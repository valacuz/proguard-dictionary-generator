package io.github.valacuz.proguard.dictionary.tasks.generate.strategy

abstract class Generator {

    protected abstract fun create(): List<String>

    fun generate(): List<String> = create().toMutableList().shuffled()
}