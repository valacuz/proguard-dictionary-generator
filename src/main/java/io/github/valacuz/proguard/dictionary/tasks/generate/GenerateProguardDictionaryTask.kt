package io.github.valacuz.proguard.dictionary.tasks.generate

import io.github.valacuz.proguard.dictionary.DictionaryGeneratorPluginExtension
import io.github.valacuz.proguard.dictionary.tasks.generate.factory.DefaultGeneratorFactory
import io.github.valacuz.proguard.dictionary.tasks.generate.factory.GeneratorFactory
import io.github.valacuz.proguard.dictionary.util.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import javax.inject.Inject

open class GenerateProguardDictionaryTask @Inject constructor(
    @get:Nested val extension: DictionaryGeneratorPluginExtension,
    @get:Input val isEnabledLog: Boolean,
) : DefaultTask() {

    private val factory: GeneratorFactory = DefaultGeneratorFactory()

    private val logger = Logger.getLogger(GenerateProguardDictionaryTask::class.java)

    @TaskAction
    @Throws(IOException::class, IllegalArgumentException::class)
    fun execute() {
        logger.useQuietLog = isEnabledLog

        // Generate fields and methods obfuscation dictionary.
        generateDictionary(
            extension.fieldMethodObfuscationStrategy.name,
            "fields and methods",
            project.getFieldClassDictionaryFile()
        )
        // Generate classes obfuscation dictionary.
        generateDictionary(
            extension.classObfuscationStrategy.name,
            "classes",
            project.getClassDictionaryFile()
        )
        // Generate packages obfuscation dictionary.
        generateDictionary(
            extension.packageObfuscationStrategy.name,
            "packages",
            project.getPackageDictionaryFile()
        )

        // Write configuration file if needed.
        if (extension.createConfigFile) {
            writeDictionaryConfigurationFile(project.getConfigurationFile(extension.configFilePath))
        }
    }

    @Throws(IOException::class)
    private fun generateDictionary(strategyName: String, dictionaryName: String, file: File) {
        val generator = factory.createByName(strategyName)
        val dict = generator.generate()
        val parent = file.parentFile ?: return

        logger.info("$TAG: Generated obfuscation dictionary for $dictionaryName with ${generator.javaClass.simpleName}.")
        if (parent.exists() || parent.mkdirs()) {
            file.writeText(dict.joinToString(System.lineSeparator()))
        } else {
            throw IOException("Failed to create output folder at ${file.path}.")
        }
    }

    @Throws(IOException::class)
    private fun writeDictionaryConfigurationFile(configFile: File) {
        val parent = configFile.parentFile ?: return

        if (parent.exists() || parent.mkdirs()) {
            configFile.writeText(
                getObfuscationSettingContent(
                    project.getFieldClassDictionaryFile().absolutePath,
                    project.getClassDictionaryFile().absolutePath,
                    project.getPackageDictionaryFile().absolutePath
                )
            )
        } else {
            throw IOException("Failed to create output folder at ${configFile.path}.")
        }
    }

    private fun getObfuscationSettingContent(
        fieldDictPath: String,
        classDictPath: String,
        packageDictPath: String
    ): String =
        """
        |-obfuscationdictionary $fieldDictPath
        |-classobfuscationdictionary $classDictPath
        |-packageobfuscationdictionary $packageDictPath
        |""".trimMargin()

    private fun Project.getConfigurationFile(path: String): File =
        File(this.projectDir, path)

    private fun Project.getFieldClassDictionaryFile(): File =
        File(this.projectDir, DictionaryGeneratorPluginExtension.FIELD_OBFUSCATION_FILE_PATH)

    private fun Project.getClassDictionaryFile(): File =
        File(this.projectDir, DictionaryGeneratorPluginExtension.CLASS_OBFUSCATION_FILE_PATH)

    private fun Project.getPackageDictionaryFile(): File =
        File(this.projectDir, DictionaryGeneratorPluginExtension.PACKAGE_OBFUSCATION_FILE_PATH)

    companion object {
        private const val TAG = "GenerateProguardDictionaryTask"

        const val TASK_NAME = "generateProguardDict"
    }
}
