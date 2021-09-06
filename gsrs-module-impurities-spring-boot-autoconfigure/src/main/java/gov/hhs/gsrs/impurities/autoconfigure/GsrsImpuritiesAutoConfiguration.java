package gov.hhs.gsrs.impurities.autoconfigure;

import gov.hhs.gsrs.impurities.services.SubstanceModuleService;

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
