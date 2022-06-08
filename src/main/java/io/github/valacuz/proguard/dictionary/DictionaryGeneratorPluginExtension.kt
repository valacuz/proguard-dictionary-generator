package io.github.valacuz.proguard.dictionary

import io.github.valacuz.proguard.dictionary.tasks.generate.strategy.ObfuscationStrategy
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

open class DictionaryGeneratorPluginExtension {

    companion object {
        private const val DEFAULT_OUTPUT_PATH = "build/tmp/dictionary"

        const val DEFAULT_CONFIG_FILE_PATH = "${DEFAULT_OUTPUT_PATH}/proguard_dictionary_config.txt"

        internal const val FIELD_OBFUSCATION_FILE_PATH = "${DEFAULT_OUTPUT_PATH}/field_obfuscation_dictionary.txt"

        internal const val CLASS_OBFUSCATION_FILE_PATH = "${DEFAULT_OUTPUT_PATH}/class_obfuscation_dictionary.txt"

        internal const val PACKAGE_OBFUSCATION_FILE_PATH = "${DEFAULT_OUTPUT_PATH}/package_obfuscation_dictionary.txt"
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
    var configFilePath: String = DEFAULT_CONFIG_FILE_PATH

    /**
     * Output path for obfuscation dictionary for fields and methods.
     */
    @get:Input
    val fieldMethodDictionaryPath: String = FIELD_OBFUSCATION_FILE_PATH

    /**
     * Determine obfuscation strategy for fields and methods.
     */
    @get:Input
    var fieldMethodObfuscationStrategy: ObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS

    /**
     * Output path for obfuscation dictionary for classes.
     */
    @get:Input
    val classDictionaryPath: String = CLASS_OBFUSCATION_FILE_PATH

    /**
     * Determine obfuscation strategy for classes.
     */
    @get:Input
    var classObfuscationStrategy: ObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS

    /**
     * Output path for obfuscation dictionary for packages.
     */
    @get:Input
    val packageDictionaryPath: String = PACKAGE_OBFUSCATION_FILE_PATH

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
