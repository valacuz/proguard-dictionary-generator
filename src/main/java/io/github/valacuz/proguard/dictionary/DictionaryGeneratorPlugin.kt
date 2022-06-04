package io.github.valacuz.proguard.dictionary

import org.gradle.api.Plugin
import org.gradle.api.Project

class DictionaryGeneratorPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        DictionaryGeneratorDelegate().apply(project)
    }
}