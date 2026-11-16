package gov.hhs.gsrs.impurities.indexers;

import gov.hhs.gsrs.impurities.models.Impurities;

import gov.hhs.gsrs.impurities.models.ImpuritiesDetails;
import gov.hhs.gsrs.impurities.models.ImpuritiesSubstance;
import gov.hhs.gsrs.impurities.models.ImpuritiesTesting;
import gsrs.DefaultDataSourceConfig;
import ix.core.search.text.IndexValueMaker;
import ix.core.search.text.IndexableValue;
import ix.ginas.models.v1.Substance;
import ix.core.models.Structure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
*/

import jakarta.persistence.*;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;

@Component
public class ImpuritiesIndexValueMaker implements IndexValueMaker<Impurities> {

    @Value("${substanceAPI.BaseUrl}")
    private String baseUrl;

    @PersistenceContext(unitName = DefaultDataSourceConfig.NAME_ENTITY_MANAGER)
    public EntityManager entityManager;

    private static final Pattern SPLIT = Pattern.compile("(\\s+)");

    @Override
    public Class<Impurities> getIndexedEntityClass() {
        return Impurities.class;
    }

    @Override
    public void createIndexableValues(Impurities impurities, Consumer<IndexableValue> consumer) {

        for (ImpuritiesSubstance impSub : impurities.impuritiesSubstanceList) {
            if (impSub != null) {
                if (impSub.substanceUuid != null) {
                    UUID subUuid = UUID.fromString(impSub.substanceUuid);

                    // Call getSubstance function to get Substance Object
                    Substance s = getSubstance(subUuid);

                    if (s != null) {

                        if (s.uuid != null) {
                            consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", s.uuid.toString()));
                        }

                        if (s.approvalID != null) {
                            // **** Facet: "Substance APPROVAL ID"
                            consumer.accept(IndexableValue.simpleFacetStringValue("Substance Approval ID", s.approvalID));
                        }

                        if (s.getName() != null) {
                            // **** Facet: "Ingredient Name (Preferred)"
                            consumer.accept(IndexableValue.simpleFacetStringValue("Substance Name (Preferred)", s.getName()).suggestable().setSortable());
                        }

                        s.names.forEach(nameObj -> {

                            if (nameObj.name != null) {
                                // **** Facet: "Ingredient Name"  (Note: This includes all the Ingredient Names)
                                consumer.accept(IndexableValue.simpleFacetStringValue("Substance Name", nameObj.name).suggestable().setSortable());
                            }
                        });

                    } // if substance object is present or not null

                } // if Substance UUID is not null


                for (ImpuritiesTesting impTest : impSub.impuritiesTestList) {
                    if (impTest != null) {
                        for (ImpuritiesDetails impDet : impTest.impuritiesDetailsList) {
                            if (impDet != null) {
                                if (impDet.relatedSubstanceUuid != null) {
                                    UUID relatedSubUuid = UUID.fromString(impDet.relatedSubstanceUuid);

                                    // Call getSubstance function to get Substance Object
                                    Substance s = getSubstance(relatedSubUuid);

                                    if (s != null) {
                                        if (s.getName() != null) {
                                            // **** Facet: "Impurities"
                                            consumer.accept(IndexableValue.simpleFacetStringValue("Impurities", s.getName()).suggestable().setSortable());
                                        }
                                    } // if substance object is not null

                                }  // if relatedSubstanceUuid != null
                            }
                        } // for impuritiesDetailsList
                    }
                } // for impuritiesTestList

            } // if ImpSub != null
        } // for impuritiesSubstanceList
    }

    public Substance getSubstance(UUID subUuid) {

        //Get Substance Object
        Query query = entityManager.createQuery("SELECT s FROM Substance s WHERE s.uuid=:subUuid");
        query.setParameter("subUuid", subUuid);
        Substance s = (Substance) query.getSingleResult();

        return s;
    }
}
