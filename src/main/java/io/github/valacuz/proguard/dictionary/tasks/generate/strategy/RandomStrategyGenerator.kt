package io.github.valacuz.proguard.dictionary.tasks.generate.strategy

import io.github.valacuz.proguard.dictionary.tasks.generate.factory.DefaultGeneratorFactory

class RandomStrategyGenerator : Generator() {

    private val strategies = listOf(
        ObfuscationStrategy.RANDOM_CHARACTERS,
        ObfuscationStrategy.RANDOM_WORDS,
    )

    override fun create(): List<String> {
        val strategy = strategies[strategies.indices.random()]
        return DefaultGeneratorFactory()
            .createByName(strategy.name)
            .generate()
    }
}