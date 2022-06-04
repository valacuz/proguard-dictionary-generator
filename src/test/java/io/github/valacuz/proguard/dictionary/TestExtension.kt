package io.github.valacuz.proguard.dictionary

import org.gradle.testkit.runner.GradleRunner
import org.junit.rules.TemporaryFolder
import java.io.File

fun TemporaryFolder.copyFromResourceFolder(folderName: String) {
    File(this::class.java.classLoader.getResource(folderName)!!.file)
        .copyRecursively(newFile(folderName), true)
}

fun TemporaryFolder.gradleRunner(): GradleRunner =
    GradleRunner.create()
        .forwardOutput()
        .withProjectDir(root)

fun TemporaryFolder.writeProjectBuildDotGradle(content: String) =
    newFile("build.gradle").writeText(content)

fun TemporaryFolder.writeProjectBuildDotKts(content: String) =
    newFile("build.gradle.kts").writeText(content)

fun TemporaryFolder.writeSettingDotGradle(content: String) =
    newFile("settings.gradle").writeText(content)

fun TemporaryFolder.writeSettingDotKts(content: String) =
    newFile("settings.gradle.kts").writeText(content)
