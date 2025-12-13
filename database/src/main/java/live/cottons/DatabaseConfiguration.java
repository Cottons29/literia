package live.cottons;

import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {
    @Bean
    FlywayConfigurationCustomizer custom() {
//        System.out.println( "DatabaseConfiguration.custom()");
        return config -> config.locations("classpath:db/migrations");
    }
}
