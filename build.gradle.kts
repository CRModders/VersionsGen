plugins {
    kotlin("jvm") version "2.1.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

base {
    group = "dev.crmodders"
    archivesName = project.name.lowercase()
    version = "0.2.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // CLI Commands
    implementation("com.github.ajalt.clikt:clikt:5.0.2")

    // Testing
    testImplementation(kotlin("test"))
}

application {
    mainClass = "dev.crmodders.versionsgen.MainKt"
}

// Shadow JAR automatically added this:
tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

tasks.test {
    useJUnitPlatform()
}
