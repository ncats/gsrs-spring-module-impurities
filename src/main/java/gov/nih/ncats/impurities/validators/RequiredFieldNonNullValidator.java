package gov.nih.ncats.impurities.validators;

import gov.nih.ncats.impurities.models.*;
import gov.nih.ncats.impurities.repositories.ImpuritiesRepository;
import gsrs.validator.ValidatorConfig;
import ix.core.validator.GinasProcessingMessage;
import ix.core.validator.ValidatorCallback;
import ix.ginas.utils.validation.ValidatorPlugin;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Iterator;

public class RequiredFieldNonNullValidator implements ValidatorPlugin<Impurities> {
    @Autowired
    private ImpuritiesRepository repository;

    @Override
    public boolean supports(Impurities newValue, Impurities oldValue, ValidatorConfig.METHOD_TYPE methodType) {
        return methodType != ValidatorConfig.METHOD_TYPE.BATCH;
    }

    @Override
    public void validate(Impurities objnew, Impurities objold, ValidatorCallback callback) {
        if (objnew.impuritiesSubstanceList.isEmpty()) {
            GinasProcessingMessage mes = GinasProcessingMessage
                    .ERROR_MESSAGE("Must have at least one Substance");
            callback.addMessage(mes);
        }
        Iterator<ImpuritiesSubstance> iter = objnew.impuritiesSubstanceList.iterator();
        while(iter.hasNext()) {
            ImpuritiesSubstance s  = iter.next();
            if (s == null) {
                callback.addMessage(GinasProcessingMessage
                        .ERROR_MESSAGE("Must have at least one Substance"));
            }
            if (s.substanceUuid == null) {
                callback.addMessage(GinasProcessingMessage
                        .ERROR_MESSAGE("Substance Name is required"));
            }
            if (s.substanceUuid != null && s.substanceUuid.length() < 1) {
                callback.addMessage(GinasProcessingMessage
                        .ERROR_MESSAGE("Substance Name is required"));
            }
        }
    }

}
