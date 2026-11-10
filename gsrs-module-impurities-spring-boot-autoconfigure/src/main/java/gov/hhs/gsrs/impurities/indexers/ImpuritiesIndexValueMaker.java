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

                    // Call getStructure function to get Substance Object
                   // Structure struct = getStructure(subUuid);

                    /*
                    if (struct != null) {
                        System.out.println("********************************" + struct.id);
                        // **** Facet: "Structure Smiles"
                        //  consumer.accept(IndexableValue.simpleFacetStringValue("Smiles", s.structure.smiles));
                    }
                    */

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

    /*
    public Structure getStructure(UUID subUuid) {

        //Get Substance Object
        Query query = entityManager.createQuery("SELECT s FROM Structure s WHERE s.id=:subUuid");
        query.setParameter("subUuid", subUuid);
        Structure s = (Structure) query.getSingleResult();

        return s;
    }

    public Boolean substanceExists(String uuid) {
        System.out.println("Inside "+ "substanceExists " + uuid);

        // is there a way to make this final and use property?
        String urlTemplate1 = baseUrl +  "api/v1/substances(%s)";
        Boolean exists;
        if (uuid == null) return null;
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.getForEntity(String.format(urlTemplate1, uuid), String.class);
        } catch(HttpClientErrorException e) {
            // this is wierd. 404 will generate exception.
            e.printStackTrace();
            return null;
        }
        if(response == null) return null;

        HttpStatus statusCode = response.getStatusCode();
        if (statusCode == null)  return null;

        if (statusCode.equals(HttpStatus.valueOf(404))) {
            return false;
        }
        if (statusCode.equals(HttpStatus.OK)) {
            JsonNode root = null;
            try {
                root = objectMapper.readTree(response.getBody());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
            // Should not be necessary, but possible to get 200 and valid json
            // on a redirect.
            JsonNode name = root.path("uuid");
            if (name != null &&  name.asText().equals(uuid)) {
                return true;
            }
        }
        return null;
    }
    */
}
