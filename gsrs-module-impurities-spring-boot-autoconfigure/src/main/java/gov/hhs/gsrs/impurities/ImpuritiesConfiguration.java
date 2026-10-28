package gov.hhs.gsrs.impurities;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@AutoConfigureAfter({DataJpaRepositoriesAutoConfiguration.class})
@Import(ImpuritiesStarterEntityRegistrar.class)
public class ImpuritiesConfiguration {
}
