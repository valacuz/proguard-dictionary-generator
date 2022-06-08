package io.github.valacuz.proguard.dictionary

class TestHelper {

    fun getDefaultSettingDotGradleContent(
        projectName: String
    ): String {
        return """
    |dependencyResolutionManagement {
    |  repositories {
    |    mavenCentral()
    |    google()
    |  }
    |}
    |include ":$projectName"
    |""".trimMargin()
    }

    fun getDefaultSettingDotKtsContent(
        projectName: String
    ): String {
        return """
    |pluginManagement {
    |  repositories {
    |    mavenCentral()
    |    google()
    |  }
    |}
    |include (":$projectName")
    |""".trimMargin()
    }

    fun getDefaultProjectBuildDotGradleContent(
        agpVersion: String = "4.2.1",
        pluginVersion: String = "1.0.1"
    ) = """
    |buildscript {
    |  repositories {
    |    google()
    |    mavenCentral()
    |    mavenLocal() // requires for local test
    |  }
    |  dependencies {
    |    classpath "com.android.tools.build:gradle:$agpVersion"
    |    classpath "io.github.valacuz:proguard-dict-generator:$pluginVersion"
    |  }
    |}
    |""".trimMargin()

    fun getDefaultProjectBuildDotKtsContent(
        agpVersion: String = "4.2.1",
        kotlinVersion: String = "1.4.32",
        pluginVersion: String = "1.0.1"
    ) = """
    |buildscript {
    |  repositories {
    |    google()
    |    mavenCentral()
    |    mavenLocal()
    |  }
    |  dependencies {
    |    classpath("com.android.tools.build:gradle:$agpVersion")
    |    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    |    classpath("io.github.valacuz:proguard-dict-generator:$pluginVersion")
    |  }
    |}
    |""".trimMargin()

    fun getAndroidModuleBuildDotGradleContent(
        buildTypesContent: String,
        produceFlavorsContent: String = "",
        pluginContent: String = ""
    ) = """
    |import io.github.valacuz.proguard.dictionary.tasks.generate.strategy.*
    |
    |plugins {
    |  id 'com.android.application'
    |  id 'io.github.valacuz.proguard-dictionary-generator'
    |}
    |android {
    |  compileSdk 31
    |  defaultConfig {
    |    applicationId "com.valacuz.sample"
    |    minSdk 23
    |    targetSdk 31
    |    versionCode 1
    |    versionName "1.0"
    |    testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    |  }
    |  $buildTypesContent
    |  $produceFlavorsContent
    |}
    |$pluginContent
    |""".trimMargin()

    fun getDefaultBuildTypesContent() = """
    |buildTypes {
    |  debug {
    |    minifyEnabled false
    |  }
    |  release {
    |    minifyEnabled true
    |    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    |  }
    |}
    |""".trimMargin()

    fun getDefaultProduceFlavorsContent() = """
    |flavorDimensions "version"
    |productFlavors {
    |  staging {
    |    dimension "version"
    |    applicationIdSuffix ".staging"
    |    versionNameSuffix "-staging"
    |  }
    |  production {
    |    dimension "version"
    |  }
    |}
    |""".trimMargin()

    fun getObfuscationConfigContent(
        fieldMethodDictionaryPath: String,
        classDictionaryPath: String,
        packageDictionaryPath: String
    ) = """
    |-obfuscationdictionary $fieldMethodDictionaryPath
    |-classobfuscationdictionary $classDictionaryPath
    |-packageobfuscationdictionary $packageDictionaryPath
    |""".trimMargin()
}
