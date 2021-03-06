package gov.hhs.gsrs.impurities;

import gov.hhs.gsrs.impurities.controllers.ImpuritiesController;
import gov.hhs.gsrs.impurities.LegacyImpuritiesSearcher;
import gov.hhs.gsrs.impurities.services.ImpuritiesEntityService;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ImpuritiesSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                ImpuritiesEntityService.class.getName(),
                LegacyImpuritiesSearcher.class.getName(),
                ImpuritiesController.class.getName()
        };
    }
}
