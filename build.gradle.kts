allprojects {
    group = "org.wings"
    version = "0.1"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

tasks {
    withType<Wrapper> {
        gradleVersion = "5.6"
        distributionType = Wrapper.DistributionType.ALL
    }
}
