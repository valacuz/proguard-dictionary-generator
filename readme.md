# Proguard dictionary generator

This plugin creates dictionaries for Proguard / R8 [code obfuscation process](https://developer.android.com/studio/build/shrink-code#obfuscate).



## Table of content
  * [Installation](#installation)
  * [Configuration](#configuration)
  * [How plugin works](#how-plugin-works)
  * [Limitation](#limitation)

<br/>
<br/>

## Installation
1. Make sure you enable minify on your android application module and [customize keep rules](https://developer.android.com/studio/build/shrink-code#keep-code).

2. For later version of plugin DSL, you can look for installation guide at [plugin page on Gradle](https://plugins.gradle.org/plugin/io.github.valacuz.proguard-dictionary-generator).

3. Add the plugin as dependency on root project's `build.gradle`.

    For **Groovy**,
    ```groovy
    buildscript {

        dependencies {
            classpath "io.github.valacuz:proguard-dict-generator:1.0.0"
        }
    }
    ```

    For **Kotlin**,
    ```kotlin
    buildscript {

        dependencies {
            classpath ("io.github.valacuz:proguard-dict-generator:1.0.0")
        }
    }
    ```

4. Apply plugin on app module's `build.gradle`.

    For **Groovy**,
    ```groovy
    plugins {
        id 'io.github.valacuz.proguard-dictionary-generator'
    }
    ```

    For **Kotlin**,
    ```kotlin
    plugins {
        id("io.github.valacuz.proguard-dictionary-generator")
    }
    ```

5. Configure obfuscation dictionary. 

    5A. By default, plugin creates a configuration file which is able to add on `proguardFiles()` on app module's `build.gradle` collection, and it is still able to build application without error even file is missing.

    For **Groovy**,
    ```groovy
    buildTypes {
        release {
            minifyEnabled true

            proguardFiles (
                getDefaultProguardFile('proguard-android-optimize.txt'),
                'proguard-rules.pro',
                'build/tmp/dictionary/proguard_dictionary_config.txt' // Add config file
            )
        }
    }
    ```

    For **Kotlin**,
    ```kotlin
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true

            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "build/tmp/dictionary/proguard_dictionary_config.txt" // Add config file
            )
        }
    }
    ```

    The path `build/tmp/dictionary/proguard_dictionary_config.txt` is output path of configuration file, and it's hard-coded.

    ## alternative way

    5B. Add it manually on app module's `proguard-rules.pro` but make sure file is exists or code obfuscation task will fail.
    ```
    -obfuscationdictionary build/tmp/dictionary/field_obfuscation_dictionary.txt
    -classobfuscationdictionary build/tmp/dictionary/class_obfuscation_dictionary.txt
    -packageobfuscationdictionary build/tmp/dictionary/package_obfuscation_dictionary.txt
    ```

6. After perform gradle sync. You can verify tasks are registered by run `./gradlew tasks` (for Windows, `.\gradlew tasks`). There should be message like this on task output (generated task name are base on build variant configuration).
    ```
    Build tasks
    -----------
    generateProguardDictRelease - Generates dictionary for Proguard or R8 code obfuscation.
    ```

7. Don't forget to upload `mapping.txt` to Google Play Store or if you build an AAB (Android App Bundle) file, mapping is included in the archive.

[Go to table of content](#table-of-content)
<br/>
<br/>


## Configuration
The plugin prepared default configuration for ready-to-use, but you are able to customize it by add following block in app module's `build.gradle`.

For **Groovy**,
```groovy
proguardDictGenerator {
    createConfigFile = true
    fieldMethodObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    classObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    packageObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    variantNameFilter = null
}
```

For **Kotlin**,
```kotlin
extensions.configure(DictionaryGeneratorPluginExtension::class) {
    createConfigFile = true
    fieldMethodObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    classObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    packageObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    variantNameFilter = null
}
```


[Go to table of content](#table-of-content)
<br/>
<br/>


## How plugin works
This plugin creates `generateProguardDict-` task when apply on android application project. These task creates files on `build/tmp/dictionary` on project directory followings.

- `field_obfuscation_dictionary.txt` is an obfuscation dictionary for field names and method names.

- `class_obfuscation_dictionary.txt` is an obfuscation dictionary for class names.

- `package_obfuscation_dictionary.txt` is an obfuscation dictionary for package names.

- `dictionary_configuration.txt` contains dictionary configuration for proguard / R8, 

    Example
    ```
    -obfuscationdictionary field_obfuscation_dictionary.txt
    -classobfuscationdictionary class_obfuscation_dictionary.txt
    -packageobfuscationdictionary package_obfuscation_dictionary.txt
    ```


These files use as a parameter for code obfuscation process. You can find more explanation [here](https://www.guardsquare.com/manual/configuration/usage#obfuscationoptions).


[Go to table of content](#table-of-content)
<br/>
<br/>


## Limitation
- Only support on android application project.
- The plugin not able to apply on subproject. You have to apply plugin on each subproject manually.


[Go to table of content](#table-of-content)
<br/>
<br/>


### Author
Kritpapon Thitichaimongkhol
