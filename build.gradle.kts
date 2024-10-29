plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "me.heyner"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

var modelMapperVersion = "3.2.0"

var jjwtVersion = "0.12.6"

var h2Version = "2.3.232"

var azureVersion = "5.17.1"

var springDocVersion = "2.6.0"

var mySqlVersion = "9.1.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.session:spring-session-core")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")
    implementation("com.azure.spring:spring-cloud-azure-dependencies:$azureVersion")
    implementation("com.azure.spring:spring-cloud-azure-starter-keyvault:$azureVersion")
    implementation("org.modelmapper:modelmapper:$modelMapperVersion")
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    compileOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.projectlombok:lombok")


    runtimeOnly("com.h2database:h2:$h2Version")
    runtimeOnly("com.mysql:mysql-connector-j:$mySqlVersion")

    testRuntimeOnly("com.h2database:h2:$h2Version")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.check {
    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = false
        html.required = false
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.check)
    violationRules {
        rule {
            limit {
                minimum = "0.5".toBigDecimal()
            }
        }
    }
}


