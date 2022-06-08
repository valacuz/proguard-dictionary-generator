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
 * This test file is to verify the plugin output on various setting.
 */
class PluginTaskConfigurationTest {

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
    fun `verify separates config uses random character strategy as default to generate dictionary`() {
        val pluginContent = """
        |proguardDictGenerator {
        |}
        |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = "",
                pluginContent = pluginContent,
            )
        )

        // When run tasks `generateProguardDictRelease`.
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictRelease", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app:generateProguardDictRelease"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and generator is `RandomCharacterGenerator`.
        assertThat(result.output)
            .contains("Generated obfuscation dictionary for fields and methods with RandomCharacterGenerator.")
        assertThat(result.output)
            .contains("Generated obfuscation dictionary for classes with RandomCharacterGenerator.")
        assertThat(result.output)
            .contains("Generated obfuscation dictionary for packages with RandomCharacterGenerator.")

        // and dictionary config output file is exists.
        var outputPath = "$androidProjectName/build/tmp/dictionary/proguard_dictionary_config.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        // and there are 3 dictionary files in output directory.
        outputPath = "$androidProjectName/build/tmp/dictionary/field_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/class_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/package_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()
    }

    @Test
    fun `verify dictionary config not file created when set createConfigFile to false`() {
        val pluginContent = """
        |proguardDictGenerator {
        |  createConfigFile = false
        |}
        |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = "",
                pluginContent = pluginContent,
            )
        )

        // When run tasks `generateProguardDictRelease`.
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictRelease", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app:generateProguardDictRelease"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and dictionary config output file is not exists.
        val outputPath = "$androidProjectName/build/tmp/dictionary/proguard_dictionary_config.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isFalse()
    }

    @Test
    fun `verify dictionary config file created when change its path`() {
        val pluginContent = """
        |proguardDictGenerator {
        |  configFilePath = "build/proguard_dictionary_config.txt"
        |}
        |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = "",
                pluginContent = pluginContent,
            )
        )

        // When run tasks `generateProguardDictRelease`.
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictRelease", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app:generateProguardDictRelease"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and dictionary config output file is exists.
        val outputPath = "$androidProjectName/build/proguard_dictionary_config.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()
    }

    @Test
    fun `verify dictionary config file created when change its path outside project file`() {
        println(File(testProjectRoot.root, "proguard_dictionary_config.txt").absolutePath)

        val pluginContent = """
        |proguardDictGenerator {
        |  configFilePath = "proguard_dictionary_config.txt"
        |}
        |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = "",
                pluginContent = pluginContent,
            )
        )

        // When run tasks `generateProguardDictRelease`.
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictRelease", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app:generateProguardDictRelease"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and dictionary config output file is exists.
        val outputPath = "$androidProjectName/proguard_dictionary_config.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()
    }

    @Test
    fun `verify separates config uses separate strategy to generate dictionary`() {
        val pluginContent = """
        |proguardDictGenerator {
        |  fieldMethodObfuscationStrategy = ObfuscationStrategy.RANDOM_WORDS
        |  classObfuscationStrategy = ObfuscationStrategy.RANDOM_WORDS
        |  packageObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
        |}
        |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = "",
                pluginContent = pluginContent,
            )
        )

        // When run tasks `generateProguardDictRelease`.
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictRelease", "-P${PARAMETER_ENABLED_LOG}")
            .build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app:generateProguardDictRelease"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and generator is `RandomCharacterGenerator`.
        assertThat(result.output)
            .contains("Generated obfuscation dictionary for fields and methods with RandomWordGenerator.")
        assertThat(result.output)
            .contains("Generated obfuscation dictionary for classes with RandomWordGenerator.")
        assertThat(result.output)
            .contains("Generated obfuscation dictionary for packages with RandomCharacterGenerator.")

        // and dictionary config file is exists.
        var outputPath = "$androidProjectName/build/tmp/dictionary/proguard_dictionary_config.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        // and there are 3 dictionary files in output directory.
        outputPath = "$androidProjectName/build/tmp/dictionary/field_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/class_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/package_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()
    }

    @Test
    fun `verify config file created with separate file for each dictionary`() {
        val pluginContent = """
        |proguardDictGenerator {
        |  createConfigFile = true
        |}
        |""".trimMargin()

        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = "",
                pluginContent = pluginContent,
            )
        )

        // When run tasks `generateProguardDictRelease`.
        val result = testProjectRoot.gradleRunner().withArguments("generateProguardDictRelease").build()

        // Then task should execute successfully.
        val assertTaskName = ":android-app:generateProguardDictRelease"
        val outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and dictionary config file exists.
        val outputPath = "$androidProjectName/build/tmp/dictionary/proguard_dictionary_config.txt"
        val configFile = File(testProjectRoot.root, outputPath)
        assertThat(configFile.exists()).isTrue()

        // and config output content is correct.
        val contents = configFile.readLines()
        assertThat(contents.size).isEqualTo(3)
        assertThat(contents[0]).contains("-obfuscationdictionary")
        assertThat(contents[0]).contains("field_obfuscation_dictionary.txt")

        assertThat(contents[1]).contains("-classobfuscationdictionary")
        assertThat(contents[1]).contains("class_obfuscation_dictionary.txt")

        assertThat(contents[2]).contains("-packageobfuscationdictionary")
        assertThat(contents[2]).contains("package_obfuscation_dictionary.txt")
    }

    @Test
    fun `should generate multiple dictionary when execute multiple tasks`() {
        File(testProjectRoot.root, "$androidProjectName/build.gradle").writeText(
            helper.getAndroidModuleBuildDotGradleContent(
                buildTypesContent = helper.getDefaultBuildTypesContent(),
                produceFlavorsContent = helper.getDefaultProduceFlavorsContent(),
                pluginContent = "",
            )
        )

        // When run tasks `generateProguardDictRelease`.
        val result = testProjectRoot.gradleRunner()
            .withArguments("generateProguardDictStagingRelease", "generateProguardDictProductionRelease")
            .build()

        // Then task should execute successfully.
        var assertTaskName = ":android-app:generateProguardDictStagingRelease"
        var outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        assertTaskName = ":android-app:generateProguardDictProductionRelease"
        outcome = result.task(assertTaskName)?.outcome
        assertThat(outcome).isEqualTo(TaskOutcome.SUCCESS)

        // and there are 3 dictionary files in output directory.
        var outputPath = "$androidProjectName/build/tmp/dictionary/field_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/class_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()

        outputPath = "$androidProjectName/build/tmp/dictionary/package_obfuscation_dictionary.txt"
        assertThat(File(testProjectRoot.root, outputPath).exists()).isTrue()
    }
}