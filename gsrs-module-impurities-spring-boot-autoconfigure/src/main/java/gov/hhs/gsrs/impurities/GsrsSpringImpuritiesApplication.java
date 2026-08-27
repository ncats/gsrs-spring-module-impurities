package gov.hhs.gsrs.impurities;

import gsrs.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableImpurities

@SpringBootApplication
@EnableGsrsApi(indexValueMakerDetector = EnableGsrsApi.IndexValueMakerDetector.CONF)
@EnableGsrsJpaEntities
@EnableGsrsLegacyAuthentication
@EnableGsrsLegacyCache
@EnableGsrsLegacyPayload
@EnableGsrsLegacySequenceSearch
@EnableGsrsLegacyStructureSearch
@EntityScan(basePackages ={"ix","gsrs", "gov.nih.ncats", "gov.hhs.gsrs"} )
@EnableJpaRepositories(basePackages ={"ix","gsrs", "gov.nih.ncats", "gov.hhs.gsrs"} )
@EnableGsrsScheduler
@EnableGsrsBackup
@EnableAsync

// SB 3x Is this file needed?
// is so, create a configuration file like in the service
public class GsrsSpringImpuritiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsrsSpringImpuritiesApplication.class, args);
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                       // .allowedOrigins("http://localhost:4200")
//                        .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS")
//                        .allowedHeaders("origin", "Content-Type", "Authorization", "Accept", "Accept-Language", "X-Authorization", "X-Requested-With", "auth-username", "auth-password", "auth-token", "auth-key", "auth-token")
//                        .allowCredentials(false).maxAge(300);
//            }
//        };
//    }
}
