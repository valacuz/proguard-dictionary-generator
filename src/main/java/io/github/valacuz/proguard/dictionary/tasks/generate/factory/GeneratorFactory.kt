package io.github.valacuz.proguard.dictionary.tasks.generate.factory

import io.github.valacuz.proguard.dictionary.tasks.generate.strategy.Generator

interface GeneratorFactory {

    fun createByName(name: String): Generator
}