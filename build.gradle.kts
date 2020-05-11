import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension


plugins {
    java
    idea
    id("org.springframework.boot") version "2.2.5.RELEASE" apply false
    id("com.google.cloud.tools.jib") version "2.1.0" apply false
    id("org.unbroken-dome.test-sets") version "3.0.1"
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

subprojects {
    apply(plugin = "java")
    apply(plugin = "groovy")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "com.google.cloud.tools.jib")
    apply(plugin = "org.unbroken-dome.test-sets")

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configure<org.unbrokendome.gradle.plugins.testsets.dsl.TestSetContainer> {
        createTestSet("integrationTest") {
            dirName = "test-integration"
            sourceSet.compileClasspath += sourceSets["main"].output + sourceSets["test"].output + configurations["testRuntimeClasspath"]
            sourceSet.runtimeClasspath += sourceSet.compileClasspath + sourceSets["test"].runtimeClasspath
        }
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
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-guava")
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testAnnotationProcessor("org.projectlombok:lombok")

        testImplementation("org.codehaus.groovy:groovy-all:2.4.15")
        testImplementation("org.spockframework:spock-core:1.2-groovy-2.4")
        testImplementation("org.spockframework:spock-spring:1.2-groovy-2.4")
        testImplementation("com.athaydes:spock-reports:1.7.1") {
            isTransitive = false
        }
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "integrationTestImplementation"("org.testcontainers:spock:1.13.0")
        "integrationTestImplementation"("org.testcontainers:mssqlserver:1.14.0")
        "integrationTestImplementation"("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
    }


    tasks {
        val check = named("check") { dependsOn("integrationTest") }
        val integrationTest = named("integrationTest", Test::class) {
            mustRunAfter("test")
            systemProperty("testContainers", project.properties["testContainers"] as String)
        }
    }

    configure<org.springframework.boot.gradle.dsl.SpringBootExtension> {
        buildInfo()
    }
}

configurations {
    all {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
}
