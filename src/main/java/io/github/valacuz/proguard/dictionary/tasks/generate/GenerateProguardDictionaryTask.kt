package io.github.valacuz.proguard.dictionary.tasks.generate

import io.github.valacuz.proguard.dictionary.DictionaryGeneratorPluginExtension
import io.github.valacuz.proguard.dictionary.tasks.generate.factory.DefaultGeneratorFactory
import io.github.valacuz.proguard.dictionary.tasks.generate.factory.GeneratorFactory
import io.github.valacuz.proguard.dictionary.util.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskOutputs
import java.io.File
import java.io.IOException
import javax.inject.Inject

open class GenerateProguardDictionaryTask @Inject constructor(
    @get:Nested val config: DictionaryGeneratorPluginExtension,
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
            config.fieldMethodObfuscationStrategy.name,
            "fields and methods",
            outputs.getFieldClassDictionaryFile()
        )
        // Generate classes obfuscation dictionary.
        generateDictionary(
            config.classObfuscationStrategy.name,
            "classes",
            outputs.getClassDictionaryFile()
        )
        // Generate packages obfuscation dictionary.
        generateDictionary(
            config.packageObfuscationStrategy.name,
            "packages",
            outputs.getPackageDictionaryFile()
        )

        // Write configuration file if needed.
        if (config.createConfigFile) {
            writeDictionaryConfigurationFile()
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
    private fun writeDictionaryConfigurationFile() {
        val configFile = outputs.getConfigurationFile()
        val parent = configFile.parentFile ?: return

        if (parent.exists() || parent.mkdirs()) {
            configFile.writeText(
                getObfuscationSettingContent(
                    outputs.getFieldClassDictionaryFile().absolutePath,
                    outputs.getClassDictionaryFile().absolutePath,
                    outputs.getPackageDictionaryFile().absolutePath
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

    private fun TaskOutputs.getConfigurationFile(): File =
        this.files.elementAt(CONFIG_FILE_INDEX)

    private fun TaskOutputs.getFieldClassDictionaryFile(): File =
        this.files.elementAt(FIELD_METHOD_OBFUSCATION_FILE_INDEX)

    private fun TaskOutputs.getClassDictionaryFile(): File =
        this.files.elementAt(CLASS_OBFUSCATION_FILE_INDEX)

    private fun TaskOutputs.getPackageDictionaryFile(): File =
        this.files.elementAt(PACKAGE_OBFUSCATION_FILE_INDEX)

    companion object {
        private const val TAG = "GenerateProguardDictionaryTask"

        const val TASK_NAME = "generateProguardDict"
        const val CONFIG_FILE_INDEX = 0
        const val FIELD_METHOD_OBFUSCATION_FILE_INDEX = 1
        const val CLASS_OBFUSCATION_FILE_INDEX = 2
        const val PACKAGE_OBFUSCATION_FILE_INDEX = 3
    }
}
