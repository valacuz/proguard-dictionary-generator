package io.github.valacuz.proguard.dictionary

import com.google.common.truth.Truth.assertThat
import io.github.valacuz.proguard.dictionary.tasks.generate.strategy.ObfuscationStrategy
import org.junit.Test

class PluginExtensionTest {

    @Test
    fun `verify default extension values`() {
        val extension = DictionaryGeneratorPluginExtension()
        extension.run {
            assertThat(variantNameFilter).isNull()
            assertThat(createConfigFile).isTrue()
            assertThat(configFilePath).isEqualTo("build/tmp/dictionary/proguard_dictionary_config.txt")
            assertThat(fieldMethodDictionaryPath).isEqualTo("build/tmp/dictionary/field_obfuscation_dictionary.txt")
            assertThat(fieldMethodObfuscationStrategy).isEqualTo(ObfuscationStrategy.RANDOM_CHARACTERS)
            assertThat(classDictionaryPath).isEqualTo("build/tmp/dictionary/class_obfuscation_dictionary.txt")
            assertThat(classObfuscationStrategy).isEqualTo(ObfuscationStrategy.RANDOM_CHARACTERS)
            assertThat(packageDictionaryPath).isEqualTo("build/tmp/dictionary/package_obfuscation_dictionary.txt")
            assertThat(packageObfuscationStrategy).isEqualTo(ObfuscationStrategy.RANDOM_CHARACTERS)
        }
    }
}
