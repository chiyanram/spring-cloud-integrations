dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
    testImplementation("com.pszymczyk.consul:embedded-consul:2.1.4")
}