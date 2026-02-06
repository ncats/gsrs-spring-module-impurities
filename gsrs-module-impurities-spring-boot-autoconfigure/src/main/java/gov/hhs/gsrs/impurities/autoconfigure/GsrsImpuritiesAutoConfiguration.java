package gov.hhs.gsrs.impurities.autoconfigure;

import gov.hhs.gsrs.impurities.services.SubstanceModuleService;

import gsrs.EnableGsrsApi;
import gsrs.EnableGsrsJpaEntities;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@EnableGsrsJpaEntities
@EnableGsrsApi
@AutoConfiguration
@Import({
         SubstanceModuleService.class
})
public class GsrsImpuritiesAutoConfiguration {
}
