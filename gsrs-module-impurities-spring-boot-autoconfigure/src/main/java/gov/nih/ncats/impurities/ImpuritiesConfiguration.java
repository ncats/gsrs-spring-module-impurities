package gov.nih.ncats.impurities;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
@Import(gov.nih.ncats.impurities.ImpuritiesStarterEntityRegistrar.class)
public class ImpuritiesConfiguration {
}
