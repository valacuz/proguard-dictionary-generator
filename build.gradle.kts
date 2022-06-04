import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    google()
}

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.18.0"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        languageVersion = "1.4"
        apiVersion = "1.4"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    val gradleBuildTools = "com.android.tools.build:gradle:7.1.2"
    val truth = "com.google.truth:truth:1.1.3"
    val junit = "junit:junit:4.13.2"

    implementation(gradleApi())
    implementation(localGroovy())
    compileOnly(gradleBuildTools)

    testImplementation(gradleTestKit())
    testImplementation(gradleBuildTools)
    testImplementation(truth)
    testImplementation(junit)
}

group = "io.github.valacuz"
version = "1.0.0"

publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = if (version.toString().endsWith("SNAPSHOT")) {
                uri(layout.buildDirectory.dir("repo/snapshot"))
            } else {
                uri(layout.buildDirectory.dir("repo/release"))
            }
        }
    }
}

pluginBundle {
    website = "https://github.com/valacuz/proguard-dictionary-generator"
    vcsUrl = "https://github.com/valacuz/proguard-dictionary-generator.git"
    tags = listOf("obfuscation", "dictionary", "proguard", "r8")
}

gradlePlugin {

    plugins {
        create("dictionaryGeneratorPlugin") {
            id = "io.github.valacuz.proguard-dictionary-generator"
            displayName = "Proguard dictionary generator"
            description = "Generates dictionary for Proguard / R8 code obfuscation."
            implementationClass = "io.github.valacuz.proguard.dictionary.DictionaryGeneratorPlugin"
        }
    }
}
