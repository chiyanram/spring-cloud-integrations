import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension


plugins {
    java
    idea
    id("org.springframework.boot") version "2.2.5.RELEASE" apply false
    id("com.google.cloud.tools.jib") version "2.1.0" apply false
}

repositories {
    mavenCentral()
}

group = "com.rmurugaian.spring.cloud"

allprojects {
    apply(plugin = "idea")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.RELEASE")
        }
    }
}


val junitJupiterVersion = "5.4.0"

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "com.google.cloud.tools.jib")

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configure<com.google.cloud.tools.jib.gradle.JibExtension> {
        from {
            image = "adoptopenjdk/openjdk11"
        }
        to {
            image = "$project.dockerRepo/$project.name"
            tags = setOf(project.version as String)
        }
        container {
            creationTime = "USE_CURRENT_TIMESTAMP"
            jvmFlags = listOf("-Djava.security.egd=file:/dev/./urandom")
        }
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-actuator")

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testAnnotationProcessor("org.projectlombok:lombok")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("junit:junit")
        testImplementation(platform("org.junit:junit-bom:$junitJupiterVersion"))
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
        testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
        testRuntime("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            showExceptions = true
            showStandardStreams = true
            events(org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED, org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED, org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED)
        }
    }
}