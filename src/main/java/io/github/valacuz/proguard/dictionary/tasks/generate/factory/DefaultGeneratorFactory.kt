package io.github.valacuz.proguard.dictionary.tasks.generate.factory

import io.github.valacuz.proguard.dictionary.tasks.generate.strategy.*

open class DefaultGeneratorFactory : GeneratorFactory {

    override fun createByName(name: String): Generator {
        return when (name) {
            ObfuscationStrategy.RANDOM.name ->
                RandomStrategyGenerator()
            ObfuscationStrategy.RANDOM_CHARACTERS.name ->
                RandomCharacterGenerator()
            ObfuscationStrategy.RANDOM_WORDS.name ->
                RandomWordGenerator()
            else ->
                throw IllegalArgumentException("No generator found for specific name.")
        }
    }
}