plugins {
    java
    maven
}

description = "General purpose Pega PRCP remote executor"

dependencies {

    implementation("org.stagemonitor:stagemonitor-configuration:0.89.0")

    implementation("org.slf4j:slf4j-api:1.7.25")

    implementation("org.apache.tika:tika-core:1.22")
    implementation("com.squareup.okhttp3:okhttp:4.1.0")
//    implementation("io.github.classgraph:classgraph:4.8.46")
    implementation("io.github.classgraph:classgraph:4.8.40")
//    implementation("io.github.classgraph:classgraph:4.6.32")

    testImplementation("org.assertj:assertj-core:3.13.2")

    testImplementation("org.ow2.asm:asm:7.1")
    testImplementation("org.mockito:mockito-core:3.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")

    
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
