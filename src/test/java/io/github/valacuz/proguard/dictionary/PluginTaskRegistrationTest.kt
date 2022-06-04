package io.github.valacuz.proguard.dictionary

import com.google.common.truth.Truth.assertThat
import io.github.valacuz.proguard.dictionary.DictionaryGeneratorDelegate.Companion.PARAMETER_ENABLED_LOG
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * This test file is to verify the plugin register task correctly on build types and build variants.
 */
class PluginTaskRegistrationTest {

    @get:Rule
    var testProjectRoot = TemporaryFolder()

    private val helper = TestHelper()

    private val androidProjectName = "android-app"

    @Before
    fun `prepare android application project`() {
        testProjectRoot.run {
            val settingGradleContent = helper.getDefaultSettingDotGradleContent(androidProjectName)
            val projectBuildGradleContent = helper.getDefaultProjectBuildDotGradleContent()

            copyFromResourceFolder(androidProjectName)
            writeSettingDotGradle(settingGradleContent)
            writeProjectBuildDotGradle(projectBuildGradleContent)
        }
    }

    @Test
    fun `create task on build type enables minified`() {
        // Use android-app default's build.gradle file with 2 buildTypes
        // 1. debug which disable minification
        // 2. release which enables minification
        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = "",
                pluginContent = "",
            )
        )

        // When run tasks `generateProguardDictRelease`
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictRelease", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app:generateProguardDictRelease"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and there is output file in build/tmp folder
        var outputPath = "$androidProjectName/build/tmp/dictionary/field_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/class_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/package_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()
    }

    @Test
    fun `create task on custom build types enables minified`() {
        // Overwrite android-app default's build.gradle file with 3 buildTypes
        // 1. debug which disable minification
        // 2. minified which enables minification
        // 3. release which enables minification
        val buildTypesContent = """
            |buildTypes {
            |  debug {
            |    minifyEnabled false
            |  }
            |  minified {
            |    minifyEnabled true
            |    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            |  }
            |  release {
            |    minifyEnabled true
            |    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            |  }
            |}
            |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = buildTypesContent,
                produceFlavorsContent = "",
                pluginContent = "",
            )
        )

        // When run tasks `generateProguardDictMinified`
        var result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictMinified", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        var assertTaskName = ":android-app:generateProguardDictMinified"
        var outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // Delete output file from `generateProguardDictMinified` to reset output for next task execution.
        testProjectRoot.root.delete()

        // When run tasks `generateProguardDictRelease`
        result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictRelease", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        assertTaskName = ":android-app:generateProguardDictRelease"
        outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)
    }

    @Test
    fun `create task on build type enable minified and apply product flavors`() {
        // Use android-app default's build.gradle file with 2 buildTypes
        // 1. debug which disable minification
        // 2. release which enables minification
        //
        // with 2 productFlavors
        // 1. staging
        // 2. production
        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = helper.getDefaultProduceFlavorsContent(),
                pluginContent = "",
            )
        )

        // When run tasks `help` to apply plugin.
        val helpResult = testProjectRoot.gradleRunner()
            .withArguments("-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then plugin output should show message that task created for stagingRelease and productionRelease variant.
        assertThat(helpResult.output).contains("Registering task generateProguardDictStagingRelease")
        assertThat(helpResult.output).contains("Registering task generateProguardDictProductionRelease")

        // When run tasks `generateProguardDictStagingRelease`
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictStagingRelease", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app:generateProguardDictStagingRelease"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and there is output file in build/tmp folder
        var outputPath = "$androidProjectName/build/tmp/dictionary/field_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/class_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/package_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()
    }

    @Test
    fun `create task on custom build types enables minified and apply product flavors`() {
        // Overwrite android-app default's build.gradle file with 3 buildTypes
        // 1. debug which disable minification
        // 2. minified which enables minification
        // 3. release which enables minification
        //
        // with 2 productFlavors
        // 1. staging
        // 2. production
        val buildTypesContent = """
            |buildTypes {
            |  debug {
            |    minifyEnabled false
            |  }
            |  minified {
            |    minifyEnabled true
            |    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            |  }
            |  release {
            |    minifyEnabled true
            |    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            |  }
            |}
            |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = buildTypesContent,
                produceFlavorsContent = helper.getDefaultProduceFlavorsContent(),
                pluginContent = "",
            )
        )
        // When run tasks `help` to apply plugin.
        val helpResult = testProjectRoot.gradleRunner()
            .withArguments("-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then plugin output should show message that task created for stagingMinified, productionMinified, stagingRelease and productionRelease variant.
        assertThat(helpResult.output).contains("Registering task generateProguardDictStagingMinified")
        assertThat(helpResult.output).contains("Registering task generateProguardDictProductionMinified")
        assertThat(helpResult.output).contains("Registering task generateProguardDictStagingRelease")
        assertThat(helpResult.output).contains("Registering task generateProguardDictProductionRelease")

        // When run tasks `generateProguardDictProductionMinified`
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictProductionMinified", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app:generateProguardDictProductionMinified"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and there is output file in build/tmp folder
        var outputPath = "$androidProjectName/build/tmp/dictionary/field_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/class_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/package_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()
    }

    @Test
    fun `create task on custom build types, apply product flavors and filter with variant name`() {
        // Overwrite android-app default's build.gradle file with 3 buildTypes
        // 1. debug which disable minification
        // 2. minified which enables minification
        // 3. release which enables minification
        //
        // with 2 productFlavors
        // 1. staging
        // 2. production
        //
        // and apply variant filter ends with "Release"
        val buildTypesContent = """
            |buildTypes {
            |  debug {
            |    minifyEnabled false
            |  }
            |  minified {
            |    minifyEnabled true
            |    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            |  }
            |  release {
            |    minifyEnabled true
            |    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            |  }
            |}
            |""".trimMargin()

        val pluginContent = """
        |proguardDictGenerator {
        |  variantNameFilter = "(.*)Release"
        |}
        |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = buildTypesContent,
                produceFlavorsContent = helper.getDefaultProduceFlavorsContent(),
                pluginContent = pluginContent,
            )
        )
        // When run tasks `help` to apply plugin.
        val helpResult = testProjectRoot.gradleRunner()
            .withArguments("-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then plugin output should show message that task created for stagingRelease and productionRelease variant.
        assertThat(helpResult.output).contains("Registering task generateProguardDictStagingRelease")
        assertThat(helpResult.output).contains("Registering task generateProguardDictProductionRelease")

        // and make sure that no task created for stagingMinified and productionMinified.
        assertThat(helpResult.output).doesNotContain("Registering task generateProguardDictStagingMinified")
        assertThat(helpResult.output).doesNotContain("Registering task generateProguardDictProductionMinified")

        // When run tasks `generateProguardDictProductionRelease`
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictProductionRelease", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app:generateProguardDictProductionRelease"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and there is output file in build/tmp folder
        var outputPath = "$androidProjectName/build/tmp/dictionary/field_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/class_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/package_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()
    }

    @Test
    fun `not create task because no minified build type`() {
        // Overwrite android-app default's build.gradle file with 2 buildTypes both of them disable minification.
        val buildTypesContent = """
            |buildTypes {
            |  debug {
            |    minifyEnabled false
            |  }
            |  release {
            |    minifyEnabled false
            |    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            |  }
            |}
            |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = buildTypesContent,
                produceFlavorsContent = "",
                pluginContent = "",
            )
        )

        // When run tasks `help` to apply plugin.
        val helpResult = testProjectRoot.gradleRunner()
            .withArguments("-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then plugin output should show message that task not created because there are no minified variant.
        assertThat(helpResult.output).contains("Not found application variant matched the criteria on project $androidProjectName, skip creating task.")
    }

    @Test
    fun `not create task because no build variant name matched`() {
        // Use android-app default's build.gradle file with 2 buildTypes
        // 1. debug which disable minification
        // 2. release which enables minification
        //
        // and apply variant filter ends with "Random" (should not match any)
        val pluginContent = """
        |proguardDictGenerator {
        |  variantNameFilter = "(.*)Random"
        |}
        |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = "",
                pluginContent = pluginContent,
            )
        )

        // When run tasks `help` to apply plugin.
        val helpResult = testProjectRoot.gradleRunner()
            .withArguments("-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then plugin output should show message that task not created because there are no build variant name matched.
        assertThat(helpResult.output).contains("Not found application variant matched the criteria on project $androidProjectName, skip creating task.")
    }

    @Test
    fun `not create task because no no minified build type even build variant name matched`() {
        // Use android-app default's build.gradle file with 2 buildTypes
        // 1. debug which disable minification
        // 2. release which enables minification
        //
        // and apply variant filter ends with "Debug"
        val pluginContent = """
        |proguardDictGenerator {
        |  variantNameFilter = "(.*)Debug"
        |}
        |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = "",
                pluginContent = pluginContent,
            )
        )

        // When run tasks `help` to apply plugin.
        val helpResult = testProjectRoot.gradleRunner()
            .withArguments("-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then plugin output should show message that task not created because there are no minified variant.
        assertThat(helpResult.output).contains("Not found application variant matched the criteria on project $androidProjectName, skip creating task.")
    }
}
