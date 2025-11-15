package com.agrobalance.config;

import org.flywaydb.core.api.exception.FlywayValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class FlywayDevCleanOnValidationConfig {
    private static final Logger log = LoggerFactory.getLogger(FlywayDevCleanOnValidationConfig.class);

    @Bean
    public FlywayMigrationStrategy cleanOnValidationErrorStrategy() {
        return flyway -> {
            try {
                log.info("[Flyway DEV] Validating migrations...");
                flyway.validate();
                log.info("[Flyway DEV] Validation OK. Migrating...");
                flyway.migrate();
            } catch (FlywayValidateException ex) {
                log.warn("[Flyway DEV] Validation failed. Cleaning schema and re-running migrations. Reason: {}", ex.getMessage());
                flyway.clean();
                flyway.migrate();
                log.info("[Flyway DEV] Clean + migrate completed.");
            }
        };
    }
}
