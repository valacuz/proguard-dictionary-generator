package io.github.valacuz.proguard.dictionary

import io.github.valacuz.proguard.dictionary.tasks.generate.strategy.ObfuscationStrategy
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

open class DictionaryGeneratorPluginExtension {

    companion object {
        const val DEFAULT_OUTPUT_PATH = "build/tmp/dictionary"

        const val DEFAULT_CONFIG_FILE_NAME = "proguard_dictionary_config.txt"

        const val FIELD_OBFUSCATION_FILE_NAME = "field_obfuscation_dictionary.txt"

        const val CLASS_OBFUSCATION_FILE_NAME = "class_obfuscation_dictionary.txt"

        const val PACKAGE_OBFUSCATION_FILE_NAME = "package_obfuscation_dictionary.txt"

        @JvmStatic
        val defaultConfigFileName = "$DEFAULT_OUTPUT_PATH/$DEFAULT_CONFIG_FILE_NAME"
    }

    /**
     * Allows plugin create obfuscation dictionary configuration as a new file or not.
     * This helps you to configure dictionaries path without adding it manually on proguard file
     * and keep its content for proguard rules.
     */
    @get:Input
    var createConfigFile: Boolean = true

    /**
     * Output path of the obfuscation dictionary configuration file when [createConfigFile] is set to `true`.
     */
    @get:Input
    val configFilePath: String = "${DEFAULT_OUTPUT_PATH}/${DEFAULT_CONFIG_FILE_NAME}"

    /**
     * Output path for obfuscation dictionary for fields and methods.
     */
    @get:Input
    val fieldMethodDictionaryPath: String = "$DEFAULT_OUTPUT_PATH/$FIELD_OBFUSCATION_FILE_NAME"

    @get:Input
    val fieldMethodDictionaryFileName = FIELD_OBFUSCATION_FILE_NAME

    /**
     * Determine obfuscation strategy for fields and methods.
     */
    @get:Input
    var fieldMethodObfuscationStrategy: ObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS

    /**
     * Output path for obfuscation dictionary for classes.
     */
    @get:Input
    val classDictionaryPath: String = "$DEFAULT_OUTPUT_PATH/$CLASS_OBFUSCATION_FILE_NAME"

    @get:Input
    val classDictionaryFileName = CLASS_OBFUSCATION_FILE_NAME

    /**
     * Determine obfuscation strategy for classes.
     */
    @get:Input
    var classObfuscationStrategy: ObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS

    /**
     * Output path for obfuscation dictionary for packages.
     */
    @get:Input
    val packageDictionaryPath: String = "$DEFAULT_OUTPUT_PATH/$PACKAGE_OBFUSCATION_FILE_NAME"

    @get:Input
    val packageDictionaryFileName = PACKAGE_OBFUSCATION_FILE_NAME

    /**
     * Determine obfuscation strategy for packages.
     */
    @get:Input
    var packageObfuscationStrategy: ObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS

    /**
     * Filter variant name to generate dictionary task.
     *
     * By default, plugin will create task on variants which set minified to `true`.
     * This option will apply a filter to create task on variant name matches with regex.
     * for example,
     * - `(.*)Release` matches variants name ends with "Release"
     * - `Minified(*.)` matches variants name start with "Minified"
     */
    @get:Input
    @get:Optional
    var variantNameFilter: String? = null
}
