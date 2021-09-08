import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    `java-library`
    `signing`
    `idea`
    kotlin("jvm") version "1.5.21"
    kotlin("kapt") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    id("org.jmailen.kotlinter") version "3.6.0"
    id("pl.allegro.tech.build.axion-release") version "1.13.3"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.10"
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "0.1.15"
}

group = "se.svt.oss"
project.version = scmVersion.version

tasks.test {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {

    implementation(kotlin("reflect", "1.5.21"))
    implementation("me.alexpanov:free-port-finder:1.1.1")
    api("org.signal:embedded-redis:0.8.0")
    api("org.junit.jupiter:junit-jupiter-api:5.7.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("redis.clients:jedis:3.7.0")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("io.mockk:mockk:1.12.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "11"
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.2"
}
