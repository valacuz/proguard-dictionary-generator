package io.github.valacuz.proguard.dictionary.tasks.generate

import io.github.valacuz.proguard.dictionary.DictionaryGeneratorConfig
import io.github.valacuz.proguard.dictionary.tasks.generate.factory.DefaultGeneratorFactory
import io.github.valacuz.proguard.dictionary.tasks.generate.factory.GeneratorFactory
import io.github.valacuz.proguard.dictionary.util.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import javax.inject.Inject

open class GenerateProguardDictionaryTask @Inject constructor(
    @get:Nested val config: DictionaryGeneratorConfig,
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
            this.outputs.files.elementAt(FIELD_METHOD_OBFUSCATION_FILE_INDEX)
        )
        // Generate classes obfuscation dictionary.
        generateDictionary(
            config.classObfuscationStrategy.name,
            "classes",
            this.outputs.files.elementAt(CLASS_OBFUSCATION_FILE_INDEX)
        )
        // Generate packages obfuscation dictionary.
        generateDictionary(
            config.packageObfuscationStrategy.name,
            "packages",
            this.outputs.files.elementAt(PACKAGE_OBFUSCATION_FILE_INDEX)
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
        val file = outputs.files.elementAt(CONFIG_FILE_INDEX)
        val parent = file.parentFile ?: return

        if (parent.exists() || parent.mkdirs()) {
            file.writeText(
                getObfuscationSettingContent(
                    config.fieldMethodDictionaryFileName,
                    config.classDictionaryFileName,
                    config.packageDictionaryFileName
                )
            )
        } else {
            throw IOException("Failed to create output folder at ${file.path}.")
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

    companion object {
        private const val TAG = "GenerateProguardDictionaryTask"

        const val TASK_NAME = "generateProguardDict"
        const val CONFIG_FILE_INDEX = 0
        const val FIELD_METHOD_OBFUSCATION_FILE_INDEX = 1
        const val CLASS_OBFUSCATION_FILE_INDEX = 2
        const val PACKAGE_OBFUSCATION_FILE_INDEX = 3
    }
}
