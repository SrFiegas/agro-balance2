plugins {
    id("java")
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.agrobalance"
version = "1.0.0"

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
}

repositories { mavenCentral() }

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}



dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
    implementation("org.flywaydb:flyway-core:11.12.0")
    implementation("org.flywaydb:flyway-database-postgresql:11.12.0")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    // Cache & Scheduling
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    // CSV / Excel
    implementation("org.apache.commons:commons-csv:1.11.0")
    implementation("org.apache.poi:poi:5.3.0")
    implementation("org.apache.poi:poi-ooxml:5.3.0")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    // Integração com balança serial
    implementation("com.fazecast:jSerialComm:2.10.4")
    // PDF
    implementation("com.github.librepdf:openpdf:1.3.39")

    // Importador Access (.mdb)
    implementation("net.sf.ucanaccess:ucanaccess:5.0.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testRuntimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
    useJUnitPlatform()
    // JDK 21 + Mockito inline: avoid self-attach warnings and ensure reflective access
    jvmArgs("-Djdk.attach.allowAttachSelf=true", "--add-opens=java.base/java.lang=ALL-UNNAMED")
}
