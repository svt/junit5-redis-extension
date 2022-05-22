import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    `java-library`
    `signing`
    `idea`
    kotlin("jvm") version "1.6.21"
    kotlin("kapt") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("org.jmailen.kotlinter") version "3.10.0"
    id("pl.allegro.tech.build.axion-release") version "1.13.7"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.16"
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "0.1.15"
}

scmVersion.tag.prefix = "release"
scmVersion.tag.versionSeparator = "-"

group = "se.svt.oss"
project.version = scmVersion.version

tasks.test {
    useJUnitPlatform()
}

dependencies {

    implementation(kotlin("reflect"))
    implementation("me.alexpanov:free-port-finder:1.1.1")
    api("org.signal:embedded-redis:0.8.2")
    api("org.junit.jupiter:junit-jupiter-api:5.8.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("uk.org.webcompere:system-stubs-jupiter:2.0.1")
    testImplementation("redis.clients:jedis:4.2.3")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("io.mockk:mockk:1.12.4")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.2"
}
