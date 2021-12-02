package gov.hhs.gsrs.impurities;

import gov.hhs.gsrs.impurities.ImpuritiesStarterEntityRegistrar;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
@Import(ImpuritiesStarterEntityRegistrar.class)
public class ImpuritiesConfiguration {
}
