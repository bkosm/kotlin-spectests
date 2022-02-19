import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    `maven-publish`
}

val versionString = "0.1.1-SNAPSHOT"
val groupIdString = "io.github.bkosm"
val artifactIdString = "spec-tests"

group = "$groupIdString.$artifactIdString"
version = versionString

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = groupIdString
            artifactId = artifactIdString
            version = versionString

            from(components["java"])
        }
    }
}
