import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.crmodders"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    // CLI Commands
    implementation("com.github.ajalt.clikt:clikt:4.2.2")

    // Hashing
    implementation("commons-codec:commons-codec:1.16.1")

    // Testing
    testImplementation(kotlin("test"))
}

tasks.shadowJar {
    archiveBaseName.set(project.name.lowercase())
    archiveVersion.set(project.version.toString())

    manifest {
        attributes["Main-Class"] = "dev.crmodders.versionsgen.MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}