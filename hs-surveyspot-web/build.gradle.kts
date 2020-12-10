plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("kapt") version "1.4.10"
    kotlin("plugin.allopen") version "1.4.20"
    id("io.micronaut.application") version "1.1.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"

    id("com.github.node-gradle.node") version "2.2.4"
}

group="org.hshekhar.surveyspot"
version="0.1.0"

node {
    version = "10.19.0"
    npmVersion = "6.14.4"
    download = false

    workDir = file("${project.buildDir}/node")
    nodeModulesDir = file("${project.projectDir}/webapp")
}

repositories {
    mavenLocal()
    mavenCentral()
}

application {
    mainClass.set("${project.group  }.ApplicationKt")
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("${project.group}.*")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.hshekhar.surveyspot:hs-surveyspot-proto:${project.version}")

    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
}

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
}

task<com.moowork.gradle.node.npm.NpmTask>("buildReact") {
    dependsOn("npmInstall")

/*    inputs.dir("$projectDir/webapp")
    outputs.dir("$projectDir/webapp/build/web")*/

    setArgs(listOf("run", "build"))

    doLast {
        copy {
            from("$projectDir/webapp/build")
            into("./build/resources/main/static")
        }
    }
}

task<Delete>("cleanReact") {
    delete("$projectDir/webapp/build")
}

tasks.getByName("build").dependsOn("buildReact")
tasks.getByName("clean").dependsOn("cleanReact")


