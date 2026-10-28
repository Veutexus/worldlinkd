import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val kotlin_version: String = project.findProperty("kotlin_version") as String
val logback_version: String = project.findProperty("logback_version") as String

kotlin {
    compilerOptions {
        // Keep bytecode compatible with the Docker runtime and CI (Java 21),
        // even when building locally with a newer JDK.
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

java {
    // Match the Kotlin jvmTarget so the fatJar runs on the Java 21 runtime.
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

plugins {
    kotlin("jvm") version "2.4.21"
    id("io.ktor.plugin") version "3.6.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.21"
}

group = "aquadx"
version = "0.0.2"

application {
    mainClass = "aquadx.ApplicationKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    fatJar {
        archiveFileName.set("worldlinkd.jar")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    // Exclude Logback from all configurations
    configurations.configureEach {
        exclude(group = "ch.qos.logback", module = "logback-classic")
    }
    runtimeOnly("org.tinylog:slf4j-tinylog:2.8.1")
    implementation("org.tinylog:tinylog-impl:2.8.1")

    // JSON
    implementation("com.alibaba.fastjson2:fastjson2-kotlin:2.0.66")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
}
