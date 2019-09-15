import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    java
    idea
    id("org.springframework.boot") version "2.1.8.RELEASE" apply false
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
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.SR3")
        }
    }
}
subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")

    java {
        sourceCompatibility = JavaVersion.VERSION_12
        targetCompatibility = JavaVersion.VERSION_12
    }
}