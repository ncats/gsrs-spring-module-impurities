package gov.hhs.gsrs.impurities.services;

import gov.hhs.gsrs.impurities.models.*;
import gov.hhs.gsrs.impurities.repositories.ImpuritiesRepository;

import gsrs.controller.IdHelpers;
import gsrs.module.substance.SubstanceEntityService;
import gsrs.repository.GroupRepository;
import gsrs.service.AbstractGsrsEntityService;
import gsrs.events.AbstractEntityCreatedEvent;
import gsrs.events.AbstractEntityUpdatedEvent;

import gsrs.validator.ValidatorConfig;
import ix.core.validator.GinasProcessingMessage;
import ix.core.validator.ValidationResponse;
import ix.core.validator.ValidationResponseBuilder;
import ix.core.validator.ValidatorCallback;
// import ix.ginas.models.v1.Substance;
// import ix.ginas.utils.GinasProcessingStrategy;
// import ix.ginas.utils.JsonSubstanceFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ix.utils.Util;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImpuritiesEntityService extends AbstractGsrsEntityService<Impurities, Long> {
    public static final String  CONTEXT = "impurities";

    public ImpuritiesEntityService() {
        super(CONTEXT,  IdHelpers.NUMBER, null, null, null);
    }

    @Autowired
    private ImpuritiesRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

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
        return objectMapper.convertValue(json, Impurities.class);
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

    /*
    @Override
    protected Application fixUpdatedIfNeeded(Application oldEntity, Application updatedEntity) {
        //force the "owner" on all the updated fields to point to the old version so the uuids are correct
        return updatedEntity;
    }
    */

    @Override
    protected Impurities fromUpdatedJson(JsonNode json) throws IOException {
        //TODO should we make any edits to remove fields?
        return objectMapper.convertValue(json, Impurities.class);
    }

    @Override
    protected List<Impurities> fromUpdatedJsonList(JsonNode list) throws IOException {
        return null;
        /*
        List<Application> substances = new ArrayList<>(list.size());
        for(JsonNode n : list){
            substances.add(fromUpdatedJson(n));
        }
        return substances;
         */
    }

    @Override
    protected JsonNode toJson(Impurities impurities) throws IOException {
        return objectMapper.valueToTree(impurities);
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

}
