plugins {
    kotlin("jvm") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

base {
    group = "dev.crmodders"
    archivesName = project.name.lowercase()
    version = "0.1.1"
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
    implementation("com.github.ajalt.clikt:clikt:4.2.2")

    // Testing
    testImplementation(kotlin("test"))
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "dev.crmodders.versionsgen.MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}
