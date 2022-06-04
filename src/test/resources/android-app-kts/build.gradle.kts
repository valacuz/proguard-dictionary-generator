import io.github.valacuz.proguard.dictionary.DictionaryGeneratorPluginExtension
import io.github.valacuz.proguard.dictionary.tasks.generate.strategy.*

plugins {
    id("com.android.application")
    id("io.github.valacuz.proguard-dictionary-generator")

    kotlin("android")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.valacuz.sample"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    val environmentDimension = "environment"
    flavorDimensions(environmentDimension)

    productFlavors {
        create("staging") {
            dimension = environmentDimension
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
        }
        create("production") {
            dimension = environmentDimension
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

extensions.configure(DictionaryGeneratorPluginExtension::class) {
    createConfigFile = true
    fieldMethodObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    classObfuscationStrategy = ObfuscationStrategy.RANDOM_WORDS
    packageObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    variantNameFilter = null
}

dependencies {
    implementation ("androidx.appcompat:appcompat:1.4.2")
}