import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

val protobufVersion : String by extra("3.12.0")
val grpcVersion: String by extra("1.32.1")
val grpcKotlinVersion: String by extra("0.1.5")
val coroutineVersion: String by extra("1.3.9")

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.4.10"
    id("com.google.protobuf") version "0.8.13"
}

group = "org.hshekhar.surveyspot"
version = "0.1.0"

sourceSets.named("main") {
    withConvention(KotlinSourceSet::class) {
        kotlin.srcDirs(
            "src/main/java",
            "src/main/kotlin",
            "${buildDir}/generated/source/proto/main/java",
            "${buildDir}/generated/source/proto/main/grpc",
            "${buildDir}/generated/source/proto/main/grpckt"
        )
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    api("com.google.protobuf:protobuf-java-util:${rootProject.extra["protobufVersion"]}")
    api("io.grpc:grpc-protobuf:${rootProject.extra["grpcVersion"]}")
    api("io.grpc:grpc-stub:${rootProject.extra["grpcVersion"]}")
    api("io.grpc:grpc-kotlin-stub:${rootProject.extra["grpcKotlinVersion"]}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")


    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${rootProject.extra["protobufVersion"]}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${rootProject.extra["grpcVersion"]}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${rootProject.extra["grpcKotlinVersion"]}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn("generateProto")
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

publishing {
    publications {
	create<MavenPublication>("maven") {
	    from(components["java"])
	}
    }
}
