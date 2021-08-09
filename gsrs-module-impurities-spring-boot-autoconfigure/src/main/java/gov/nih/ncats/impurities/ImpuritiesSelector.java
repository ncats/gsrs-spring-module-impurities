package gov.nih.ncats.impurities;

import gov.nih.ncats.impurities.controllers.ImpuritiesController;
import gov.nih.ncats.impurities.LegacyImpuritiesSearcher;
import gov.nih.ncats.impurities.services.ImpuritiesEntityService;

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
