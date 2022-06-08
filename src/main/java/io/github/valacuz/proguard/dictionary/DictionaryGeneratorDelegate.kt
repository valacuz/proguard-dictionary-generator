package io.github.valacuz.proguard.dictionary

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.valacuz.proguard.dictionary.tasks.generate.GenerateProguardDictionaryTask
import io.github.valacuz.proguard.dictionary.util.Logger
import org.gradle.api.Project

class DictionaryGeneratorDelegate {

    private val logger = Logger.getLogger(DictionaryGeneratorPlugin::class.java)

    fun apply(project: Project) {
        logger.useQuietLog = project.isEnabledLog()

        val extension = project.extensions.create(
            EXTENSION_NAME,
            DictionaryGeneratorPluginExtension::class.java,
        )

        project.afterEvaluate {
            verifyConfiguration(extension)
            prepare(this, extension)

            if (this.subprojects.isNotEmpty()) {
                prepare(this.subprojects, extension)
            }
        }
    }

    private fun prepare(project: Project, config: DictionaryGeneratorPluginExtension) {
        if (project.isAndroidAppProject()) {
            addGenerateDictionaryTask(project, config)
        } else {
            logger.info("$TAG: ⚠ The project is not an android application, skip creating task.")
        }
    }

    private fun prepare(subprojects: Set<Project>, config: DictionaryGeneratorPluginExtension) {
        val androidAppSubProject = subprojects.filter { it.isAndroidAppProject() }
        if (androidAppSubProject.isNotEmpty()) {
            androidAppSubProject.forEach {
                addGenerateDictionaryTask(it, config)
            }
        } else {
            logger.info("$TAG: ⚠ Not found android application on sub project, skip creating task.")
        }
    }

    private fun addGenerateDictionaryTask(project: Project, config: DictionaryGeneratorPluginExtension) {
        val androidExtension = project.getAndroidAppExtension()
        val enabledMinifiedVariants = androidExtension?.applicationVariants
            ?.filterMinifiedBuildType()
            ?.filterByVariantPattern(config.variantNameFilter)
            ?: emptySet()

        if (enabledMinifiedVariants.isEmpty()) {
            logger.info("$TAG: ⚠ Not found application variant matched the criteria on project ${project.name}, skip creating task.")
            return
        }

        val isEnabledLog = project.isEnabledLog()
        enabledMinifiedVariants.forEach { variant ->
            val productFlavorName = variant.flavorName
            val buildTypeName = variant.buildType.name
            val variantName = if (productFlavorName.isNullOrEmpty()) {
                buildTypeName.capitalize()
            } else {
                "${productFlavorName.capitalize()}${buildTypeName.capitalize()}"
            }
            val fullTaskName = "${GenerateProguardDictionaryTask.TASK_NAME}${variantName}"

            logger.info("$TAG: ⌛ Registering task $fullTaskName.")
            project.tasks.register(
                fullTaskName,
                GenerateProguardDictionaryTask::class.java,
                config,
                isEnabledLog,
            ).configure {
                description = "Generates dictionary for Proguard or R8 code obfuscation."
                group = "Build"

                // Registering possible output files for this task.
                outputs.files(
                    config.configFilePath,
                    config.fieldMethodDictionaryPath,
                    config.classDictionaryPath,
                    config.packageDictionaryPath,
                )
            }

            // Registering task dependency.
            variant.javaCompileProvider.configure { this.dependsOn(fullTaskName) }
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun verifyConfiguration(extension: DictionaryGeneratorPluginExtension) {
        if (extension.createConfigFile && extension.configFilePath.isBlank()) {
            throw IllegalArgumentException(
                "Please set output path for field `configFilePath` when `isCreateConfigFile` is `true`."
            )
        }

        if (extension.fieldMethodDictionaryPath.isBlank() ||
            extension.classDictionaryPath.isBlank() ||
            extension.packageDictionaryPath.isBlank()
        ) {
            throw IllegalArgumentException(
                "Please set output path for field `fieldMethodDictionaryPath`, `classDictionaryPath`, `packageDictionaryPath` when `useSameDictionary` is `false`."
            )
        }
    }

    @Suppress("DEPRECATION")
    private fun Collection<BaseVariant>.filterMinifiedBuildType(): Collection<BaseVariant> =
        this.filter { it.buildType.isMinifyEnabled }

    @Suppress("DEPRECATION")
    private fun Collection<BaseVariant>.filterByVariantPattern(pattern: String?): Collection<BaseVariant> {
        return if (pattern.isNullOrBlank()) {
            this
        } else {
            val nameRegex = Regex(pattern, RegexOption.IGNORE_CASE)
            this.filter {
                val productFlavorName = it.flavorName
                val buildTypeName = it.buildType.name
                "${productFlavorName.capitalize()}${buildTypeName.capitalize()}".matches(nameRegex)
            }
        }
    }

    private fun Project.getAndroidAppExtension(): BaseAppModuleExtension? =
        project.extensions.getByName("android") as? BaseAppModuleExtension

    private fun Project.isAndroidAppProject(): Boolean =
        project.plugins.hasPlugin("com.android.application")

    private fun Project.isEnabledLog(): Boolean =
        this.gradle.startParameter.projectProperties.containsKey(PARAMETER_ENABLED_LOG)

    companion object {
        private const val TAG = "proguardDictGenerator"

        const val EXTENSION_NAME = "proguardDictGenerator"

        const val PARAMETER_ENABLED_LOG = "ENABLE_PDG_LOGS"
    }
}