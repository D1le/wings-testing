plugins {
    java
    groovy
    maven
//    kotlin("jvm") version "1.2.71"
//    kotlin("jvm") version "1.3.50"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(project(":wings-remote"))

//    implementation(files("../libs/junit-jupiter-engine-no-service-5.5.1.jar"))
    implementation("org.junit.jupiter:junit-jupiter-api:5.5.1")

    implementation("org.junit.jupiter:junit-jupiter-engine:5.5.1")
    implementation("org.junit.platform:junit-platform-launcher:1.5.1")

    implementation("org.apache.logging.log4j:log4j-core:2.6.2")
    implementation("org.apache.logging.log4j:log4j-api:2.6.2")

}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<Test> {
        useJUnitPlatform {
            includeEngines("wings-junit-jupiter")
//            includeEngines("junit-jupiter")
        }
    }
}
