package gov.hhs.gsrs.impurities.services;

import gov.hhs.gsrs.impurities.models.*;
import gov.hhs.gsrs.impurities.repositories.ImpuritiesRepository;

import gsrs.controller.IdHelpers;
import gsrs.repository.GroupRepository;
import gsrs.service.AbstractGsrsEntityService;
import gsrs.module.substance.SubstanceEntityService;
import gsrs.events.AbstractEntityCreatedEvent;
import gsrs.events.AbstractEntityUpdatedEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// jackson databind
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

//import com.fasterxml.jackson.databind.ObjectMapper;

// jakarta
import jakarta.persistence.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class ImpuritiesEntityService extends AbstractGsrsEntityService<Impurities, Long> {
    public static final String  CONTEXT = "impurities";

    public ImpuritiesEntityService() {
        super(CONTEXT,  IdHelpers.NUMBER, null, null, null);
    }

    @Autowired
    private ImpuritiesRepository repository;

    /*@Autowired
    private ObjectMapper objectMapper;
    */

    @Autowired
    @Qualifier("legacyJsonMapper")
    private JsonMapper mapper;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Class<Impurities> getEntityClass() {
        return Impurities.class;
    }

    @Override
    public Long parseIdFromString(String idAsString) {
        return Long.parseLong(idAsString);
    }

    @Override
    protected Impurities fromNewJson(JsonNode json) throws IOException {
        return mapper.convertValue(json, Impurities.class);
    }

    @Override
    public Page page(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    protected Impurities create(Impurities impurities) {
        try {
            return repository.saveAndFlush(impurities);
        }catch(Throwable t){
            t.printStackTrace();
            throw t;
        }
    }

    @Override
    @Transactional
    protected Impurities update(Impurities impurities) {
        return repository.saveAndFlush(impurities);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    protected AbstractEntityUpdatedEvent<Impurities> newUpdateEvent(Impurities updatedEntity) {
        return null;
    }

    @Override
    protected AbstractEntityCreatedEvent<Impurities> newCreationEvent(Impurities createdEntity) {
        return null;
    }

    @Override
    public Long getIdFrom(Impurities entity) {
        return entity.getId();
    }

    @Override
    protected List<Impurities> fromNewJsonList(JsonNode list) throws IOException {
        return null;
    }

    @Override
    protected Impurities fromUpdatedJson(JsonNode json) throws IOException {
        //TODO should we make any edits to remove fields?
        return mapper.convertValue(json, Impurities.class);
    }

    @Override
    protected List<Impurities> fromUpdatedJsonList(JsonNode list) throws IOException {
        return null;
    }

    @Override
    protected JsonNode toJson(Impurities impurities) throws IOException {
        return mapper.valueToTree(impurities);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Optional<Impurities> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Impurities> flexLookup(String someKindOfId) {
        if (someKindOfId == null){
            return Optional.empty();
        }
        return repository.findById(Long.parseLong(someKindOfId));
    }

    @Override
    protected Optional<Long> flexLookupIdOnly(String someKindOfId) {
        //easiest way to avoid deduping data is to just do a full flex lookup and then return id
        Optional<Impurities> found = flexLookup(someKindOfId);
        if(found.isPresent()){
            return Optional.of(found.get().id);
        }
        return Optional.empty();
    }

    public List<SubstanceRelationship> findSubstanceRelationshipBySubstanceUuid(String substanceUuid) {
        List<SubstanceRelationship> list = repository.findSubstanceRelationshipBySubstanceUuid(substanceUuid);
        return list;
    }

    public List<Object[]> getImpuritiesLists() {
        List<Object[]> rawResults = repository.getImpuritiesWithSubstance();

        // Key: productSubstanceName + "|" + substanceUuid
        // Value: [Name, UUID, "USP|||10", "EP|||11", ...]
        Map<String, List<Object>> groupedMap = new LinkedHashMap<>();

        for (Object[] row : rawResults) {
            String name = (String) row[0];
            String uuid = (String) row[1];
            String source = (String) row[2];
            Long id = (Long) row[3]; // Adjust type if needed

            // Use ||| to separate the source and ID
            String combinedSourceAndId = source + "|||" + id;

            String key = name + "|" + uuid;

            if (!groupedMap.containsKey(key)) {
                List<Object> rowData = new ArrayList<>();
                rowData.add(name);
                rowData.add(uuid);
                groupedMap.put(key, rowData);
            }

            // Add "USP|||10" as a new column
            groupedMap.get(key).add(combinedSourceAndId);
        }

        // Convert back to List<Object[]>
        List<Object[]> finalResult = new ArrayList<>();
        for (List<Object> rowData : groupedMap.values()) {
            finalResult.add(rowData.toArray());
        }

        return finalResult;
    }
}
