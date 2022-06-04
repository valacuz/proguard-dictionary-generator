package io.github.valacuz.proguard.dictionary

import com.google.common.truth.Truth.assertThat
import io.github.valacuz.proguard.dictionary.tasks.generate.factory.DefaultGeneratorFactory
import io.github.valacuz.proguard.dictionary.tasks.generate.strategy.*
import org.junit.Assert.assertThrows
import org.junit.Test

class GeneratorFactoryTest {

    @Test
    fun `should build RandomStrategyGenerator`() {
        val factory = DefaultGeneratorFactory()
        val generator = factory.createByName(ObfuscationStrategy.RANDOM.name)
        assertThat(generator).isInstanceOf(RandomStrategyGenerator::class.java)
    }

    @Test
    fun `should build RandomCharacterGenerator`() {
        val factory = DefaultGeneratorFactory()
        val generator = factory.createByName(ObfuscationStrategy.RANDOM_CHARACTERS.name)
        assertThat(generator).isInstanceOf(RandomCharacterGenerator::class.java)
    }

    @Test
    fun `should build RandomWordGenerator`() {
        val factory = DefaultGeneratorFactory()
        val generator = factory.createByName(ObfuscationStrategy.RANDOM_WORDS.name)
        assertThat(generator).isInstanceOf(RandomWordGenerator::class.java)
    }

    @Test
    fun `should throw IllegalArgumentException when not found generator by specific name`() {
        val factory = DefaultGeneratorFactory()
        val exception = assertThrows(IllegalArgumentException::class.java) {
            factory.createByName("This name should not be used for any generator")
        }
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(exception).hasMessageThat().contains("No generator found for specific name.")
    }
}