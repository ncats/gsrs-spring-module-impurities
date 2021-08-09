package gov.nih.ncats.impurities.autoconfigure;

import gov.nih.ncats.impurities.services.SubstanceModuleService;
import gsrs.EnableGsrsApi;
import gsrs.EnableGsrsJpaEntities;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableGsrsJpaEntities
@EnableGsrsApi
@Configuration
@Import({
         SubstanceModuleService.class
})
public class GsrsImpuritiesAutoConfiguration {
}
