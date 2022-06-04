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
 * This test file is to verify the plugin register task correctly on kotlin's dsl.
 */
class PluginTaskRegistrationOnKtsTest {

    @get:Rule
    var testProjectRoot = TemporaryFolder()

    private val helper = TestHelper()

    private val androidProjectName = "android-app-kts"

    @Before
    fun `prepare android application project`() {
        testProjectRoot.run {
            val settingGradleContent = helper.getDefaultSettingDotKtsContent(androidProjectName)
            val projectBuildGradleContent = helper.getDefaultProjectBuildDotKtsContent()

            copyFromResourceFolder(androidProjectName)
            writeSettingDotKts(settingGradleContent)
            writeProjectBuildDotKts(projectBuildGradleContent)
        }
    }

    @Test
    fun `create task on build type enable minified and apply product flavors`() {
        // Use android-app-kts default's build.gradle file with 2 buildTypes
        // 1. debug which disable minification
        // 2. release which enables minification
        //
        // with 2 productFlavors
        // 1. staging
        // 2. production

        // When run tasks `help` to apply plugin.
        val helpResult = testProjectRoot.gradleRunner()
            .withArguments("-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then plugin output should show message that task created for stagingRelease and productionRelease variant.
        assertThat(helpResult.output).contains("Registering task generateProguardDictStagingRelease")
        assertThat(helpResult.output).contains("Registering task generateProguardDictProductionRelease")

        // When run tasks `generateProguardDictRelease`
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictStagingRelease", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app-kts:generateProguardDictStagingRelease"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and there is output file in build/tmp folder
        var outputPath = "$androidProjectName/build/tmp/dictionary/proguard_dictionary_config.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/field_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/class_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/package_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()
    }
}
