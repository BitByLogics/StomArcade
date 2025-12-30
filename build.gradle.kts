plugins {
    id("groovy")
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
    implementation("it.unimi.dsi:fastutil:8.5.18")
    implementation("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.4")
    implementation("dev.hollowcube:polar:1.15.0")
    implementation("ch.qos.logback:logback-classic:1.5.23")
    implementation("net.kyori:adventure-text-minimessage:4.25.0")
    implementation("net.goldenstack:trove:4.0")
    implementation("io.prometheus:simpleclient:latest.release")
    implementation("com.github.BitByLogics:Bits-ORM:1.2.0")
    implementation("com.github.BitByLogics.Bits-Utils:bits-utils-common:2.1.1")
}

tasks.test {
    useJUnitPlatform()
}