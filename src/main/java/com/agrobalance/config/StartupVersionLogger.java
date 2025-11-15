package com.agrobalance.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URL;
import java.security.CodeSource;

@Configuration
@Profile({"local", "dev"})
public class StartupVersionLogger {
  private static final Logger log = LoggerFactory.getLogger(StartupVersionLogger.class);

  @Bean
  CommandLineRunner logSpringVersions() {
    return args -> {
      try {
        Class<?> clazz = org.springframework.web.method.ControllerAdviceBean.class;
        Package pkg = clazz.getPackage();
        CodeSource cs = clazz.getProtectionDomain().getCodeSource();
        URL loc = cs != null ? cs.getLocation() : null;
        log.info("[DEBUG_LOG] Spring Framework package: {}", pkg.getName());
        log.info("[DEBUG_LOG] Spring Framework impl version: {}", pkg.getImplementationVersion());
        log.info("[DEBUG_LOG] Loaded from: {}", loc);
      } catch (Throwable t) {
        log.warn("[DEBUG_LOG] Failed to resolve Spring Framework version/location", t);
      }

      try {
        Class<?> sd = org.springdoc.core.configuration.SpringDocConfiguration.class;
        Package pkg = sd.getPackage();
        CodeSource cs = sd.getProtectionDomain().getCodeSource();
        URL loc = cs != null ? cs.getLocation() : null;
        log.info("[DEBUG_LOG] SpringDoc package: {}", pkg.getName());
        log.info("[DEBUG_LOG] SpringDoc impl version: {}", pkg.getImplementationVersion());
        log.info("[DEBUG_LOG] SpringDoc loaded from: {}", loc);
      } catch (Throwable t) {
        log.warn("[DEBUG_LOG] Failed to resolve SpringDoc version/location", t);
      }
    };
  }
}