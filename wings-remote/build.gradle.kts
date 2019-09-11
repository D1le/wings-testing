plugins {
    `java-library`
    `maven-publish`
//    id("com.github.johnrengelman.shadow") version "5.1.0"
}

description = "General purpose Pega PRPC remote executor"

dependencies {

    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    implementation("io.github.classgraph:classgraph:4.8.40")
    implementation("org.apache.tika:tika-core:1.22")
    implementation("org.slf4j:slf4j-api:${Versions.slf4j}")
    implementation("org.stagemonitor:stagemonitor-configuration:0.89.0")

    testImplementation("org.assertj:assertj-core:3.13.2")
    testImplementation("org.mockito:mockito-core:3.0.0")

    testImplementation("javax.mail:mail:1.4.7")

    testImplementation("com.squareup.okhttp3:mockwebserver:4.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")

    testRuntimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.6.2")
    testRuntimeOnly("org.apache.logging.log4j:log4j-core:2.6.2")
    testRuntimeOnly("org.apache.logging.log4j:log4j-jul:2.6.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        systemProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager")
    }
}

// ensure project is built successfully before publishing it
val build = tasks[LifecycleBasePlugin.BUILD_TASK_NAME]
tasks[PublishingPlugin.PUBLISH_LIFECYCLE_TASK_NAME].dependsOn(build)
tasks[MavenPublishPlugin.PUBLISH_LOCAL_LIFECYCLE_TASK_NAME].dependsOn(build)

publishing {
    publications {
        create<MavenPublication>("maven") {
            pom {
                name.set(provider {
                    project.description ?: "${project.group}:${project.name}"
                })
                url.set("https://junit.org/junit5/")
                scm {
                    connection.set("scm:git:git://github.com/junit-team/junit5.git")
                    developerConnection.set("scm:git:git://github.com/junit-team/junit5.git")
                    url.set("https://github.com/junit-team/junit5")
                }
                licenses {
                    license {
//                        val license: License by rootProject.extra
                        name.set("lic")
                        url.set("url")
                    }
                }
                developers {
                    developer {
                        id.set("alexey-lapin")
                        name.set("Alexey Lapin")
                        email.set("alexey-lapin@protonmail.com")
                    }
                }
            }
        }
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    dependsOn(tasks.classes)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
//    from("${project.projectDir}/src/module/$javaModuleName") {
//        include("module-info.java")
//    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

configure<PublishingExtension> {
    publications {
        named<MavenPublication>("maven") {
            from(components["java"])
            artifact(sourcesJar)
//            artifact(javadocJar)
            pom {
                description.set(provider { "Module \"${project.name}\" of JUnit 5." })
            }
        }
    }
}