package io.github.valacuz.proguard.dictionary

import io.github.valacuz.proguard.dictionary.tasks.generate.strategy.ObfuscationStrategy

open class DictionaryGeneratorPluginExtension : DictionaryGeneratorConfig() {

    override var createConfigFile: Boolean = true

    override var fieldMethodObfuscationStrategy: ObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS

    override var classObfuscationStrategy: ObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS

    override var packageObfuscationStrategy: ObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS

    override var variantNameFilter: String? = null
}
