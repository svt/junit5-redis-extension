import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("maven-publish")
    idea
    kotlin("jvm") version "1.4.32"
    kotlin("kapt") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
    id("org.jmailen.kotlinter") version "3.4.3"
    id("pl.allegro.tech.build.axion-release") version "1.10.2"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.8"
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "0.1.13"
    id("signing")
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
    implementation(kotlin("reflect", "1.4.32"))

    api("com.squareup.okhttp3:mockwebserver:3.10.0")
    implementation("me.alexpanov:free-port-finder:1.1.1")
    implementation("it.ozimov:embedded-redis:0.7.3")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.assertj:assertj-core:3.18.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    implementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    implementation("org.junit.jupiter:junit-jupiter-params:5.5.2")
    testImplementation("io.mockk:mockk:1.9.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.2"
}