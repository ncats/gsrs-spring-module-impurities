package gov.nih.ncats.impurities;

import gsrs.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableConfigurationProperties


@SpringBootApplication
@EnableGsrsApi
@EnableGsrsJpaEntities
//@EnableGsrsLegacyAuthentication
@EnableGsrsLegacyCache
@EnableGsrsLegacyPayload
@EnableGsrsLegacySequenceSearch
@EntityScan(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
@EnableJpaRepositories(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
//@EnableGsrsScheduler
//@EnableGsrsBackup
public class GsrsSpringImpuritiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsrsSpringImpuritiesApplication.class, args);
    }

    /*
    @Bean
    @ConfigurationProperties("spring.datasource")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
    */
    /*
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
     */

    /*

    @Bean
    @ConfigurationProperties("spring.datasource")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

     */

    /*
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }
    */


    /*
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@//D15311532.fda.gov:1532/SRSIDDEV");
        dataSource.setUsername("SRSCID");
        dataSource.setPassword("App4gsrs!");
        return dataSource;
    }

     */

}
