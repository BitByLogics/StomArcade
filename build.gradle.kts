import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("groovy")
    id("com.gradleup.shadow") version "9.3.0"
}

group = "net.bitbylogic"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.apache.groovy:groovy:5.0.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Minestom
    implementation("net.minestom:minestom:2025.12.20-1.21.11")

    // Misc
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("io.github.togar2:MinestomPvP:2025.12.29-1.21.11")
    implementation("it.unimi.dsi:fastutil:8.5.18")
    implementation("net.kyori:adventure-text-logger-slf4j:4.26.1")
    implementation("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.4")
    implementation("dev.hollowcube:polar:1.15.0")
    implementation("net.kyori:adventure-text-minimessage:4.25.0")
    implementation("net.goldenstack:trove:4.0")
    implementation("io.prometheus:simpleclient:latest.release")
    implementation("net.bitbylogic:kardia-api:1.0.0")
    implementation("com.github.BitByLogics.Bits-Utils:bits-utils-common:2.1.1")
    implementation("com.github.BitByLogics:Bits-RPS:b7be815810")
    implementation("com.github.BitByLogics:Bits-ORM:1.2.0")

    // Runtime
    runtimeOnly("ch.qos.logback:logback-classic:1.5.23")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}


tasks.withType<ShadowJar> {
    archiveBaseName.set("StomArcade")
    archiveClassifier.set("")

    manifest {
        attributes(
            "Main-Class" to "net.bitbylogic.stomarcade.StomArcadeServer"
        )
    }
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "net.bitbylogic.stomarcade.StomArcadeServer"
        )
    }
}