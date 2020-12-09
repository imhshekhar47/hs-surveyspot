val micronautVersion: String by extra("2.1.2")
val grpcVersion: String by extra("1.32.1")
val coroutineVersion: String by extra("1.3.9")

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("kapt") version "1.4.10"
    kotlin("plugin.allopen") version "1.4.20"
    id("io.micronaut.application") version "1.1.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "org.hshekhar.surveyspot"
version = "0.1.0"

application {
    mainClassName = "org.hshekhar.surveyspot.Server"
    mainClass.set("org.hshekhar.surveyspot.Server")
}

micronaut {
    version(rootProject.ext["micronautVersion"].toString())
    runtime("netty")
    processing {
        module(project.name)
        annotations("${project.group}.*")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}


dependencies {

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.hshekhar.surveyspot:hs-surveyspot-proto:${project.version}")

    kapt("io.micronaut.security:micronaut-security-annotations")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-http-client")

    // metrics
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")

    // logging
    runtimeOnly("ch.qos.logback:logback-classic")

    // gRPC
    implementation("io.micronaut.grpc:micronaut-grpc-server-runtime")
    implementation("io.grpc:grpc-services:$grpcVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")

    //security JWT
    implementation("io.micronaut.security:micronaut-security-jwt")

    // test
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.0")
}

/*
tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}*/
