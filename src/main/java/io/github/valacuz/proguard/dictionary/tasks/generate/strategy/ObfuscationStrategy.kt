package io.github.valacuz.proguard.dictionary.tasks.generate.strategy

enum class ObfuscationStrategy {
    /**
     * Random choose obfuscation strategy.
     */
    RANDOM,

    /**
     * Contains random unicode characters.
     */
    RANDOM_CHARACTERS,

    /**
     * Contains random word from unicode characters.
     */
    RANDOM_WORDS,
}