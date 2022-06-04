package io.github.valacuz.proguard.dictionary

import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    GeneratorFactoryTest::class,
    PluginExtensionTest::class,
    PluginTaskConfigurationTest::class,
    PluginTaskRegistrationTest::class,
    PluginTaskRegistrationOnKtsTest::class
)
@Ignore("No need to run test suite when run gradle test task.")
class TestSuite