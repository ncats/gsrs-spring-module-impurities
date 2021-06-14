package gov.nih.ncats.impurities.processors;

import gov.nih.ncats.impurities.models.Impurities;
import gov.nih.ncats.impurities.controllers.ImpuritiesController;

import gsrs.springUtils.AutowireHelper;
import ix.core.EntityProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Slf4j
public class ImpuritiesProcessor implements EntityProcessor<Impurities> {

    @Autowired
    public ImpuritiesController impuritiesController;

    @Override
    public Class<Impurities> getEntityClass() {
        return Impurities.class;
    }

    @Override
    public void prePersist(final Impurities obj) {
    }

    @Override
    public void preUpdate(Impurities obj) {
    }

    @Override
    public void preRemove(Impurities obj) {
    }

    @Override
    public void postLoad(Impurities obj) throws FailProcessingException {
        /*
        if(impuritiesController==null) {
            AutowireHelper.getInstance().autowire(this);
        }

        if (impuritiesController != null) {
            Optional<Impurities> objInjectSubstance = impuritiesController.injectSubstanceDetails(Optional.of(obj));
        }
         */
    }

}
