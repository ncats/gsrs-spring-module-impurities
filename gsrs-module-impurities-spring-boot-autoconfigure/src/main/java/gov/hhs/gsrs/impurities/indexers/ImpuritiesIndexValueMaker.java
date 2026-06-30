package gov.hhs.gsrs.impurities.indexers;

import gov.hhs.gsrs.impurities.models.Impurities;

import gov.hhs.gsrs.impurities.models.ImpuritiesSubstance;
import gsrs.DefaultDataSourceConfig;
import ix.core.search.text.IndexValueMaker;
import ix.core.search.text.IndexableValue;
import ix.ginas.models.v1.Substance;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;

@Component
public class ImpuritiesIndexValueMaker implements IndexValueMaker<Impurities> {

    @PersistenceContext(unitName = DefaultDataSourceConfig.NAME_ENTITY_MANAGER)
    public EntityManager entityManager;

    private static final Pattern SPLIT = Pattern.compile("(\\s+)");

    @Override
    public Class<Impurities> getIndexedEntityClass() {
        return Impurities.class;
    }

    @Override
    public void createIndexableValues(Impurities impurities, Consumer<IndexableValue> consumer) {

        // Facet: Ingredient Name  (Note: This includes all the Ingredient Names)
        for (ImpuritiesSubstance ImpSub : impurities.impuritiesSubstanceList) {
            if (ImpSub != null) {
                if (ImpSub.substanceUuid != null) {
                    UUID subUuid = UUID.fromString(ImpSub.substanceUuid);

                    //Get Substance Object
                    Query query = entityManager.createQuery("SELECT s FROM Substance s WHERE s.uuid=:subUuid");
                    query.setParameter("subUuid", subUuid);
                    Substance s = (Substance) query.getSingleResult();

                    if (s != null) {
                        s.names.forEach(nameObj -> {
                            if (nameObj.name != null) {
                                consumer.accept(IndexableValue.simpleFacetStringValue("Ingredient Name", nameObj.name).suggestable().setSortable());
                            }
                        });
                        // Facet: "Ingredient Name (Preferred)"
                        if (s.getName() != null) {
                            consumer.accept(IndexableValue.simpleFacetStringValue("Ingredient Name (Preferred)", s.getName()).suggestable().setSortable());
                        }

                        if (s.uuid != null) {
                            consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", s.uuid.toString()));

                            consumer.accept(IndexableValue.simpleFacetStringValue("Substance UUID", s.uuid.toString()));
                        }
                    }
                }
            }
        } // for impuritiesSubstanceList
    }
}
