## Configuration

Current version of plugin provides several configuration as below.

```kotlin
extensions.configure(DictionaryGeneratorPluginExtension::class) {
    createConfigFile = true
    configFilePath = DictionaryGeneratorPluginExtension.DEFAULT_CONFIG_FILE_PATH
    fieldMethodObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    classObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    packageObfuscationStrategy = ObfuscationStrategy.RANDOM_CHARACTERS
    variantNameFilter = null
}
```

- `createConfigFile` - Allows plugin create obfuscation dictionary configuration as a new file or not. This helps you to configure dictionaries path without adding it manually on proguard file and keep its content for proguard rules.

- `configFilePath` - Output path of the obfuscation dictionary configuration file when createConfigFile is set to `true`.

- `fieldMethodObfuscationStrategy` - Determine obfuscation strategy for fields and methods.

- `classObfuscationStrategy` - Determine obfuscation strategy for classes.

- `packageObfuscationStrategy` - Determine obfuscation strategy for packages.

- `variantNameFilter` - Filter variant name to generate dictionary task. By default, plugin will create task on variants which set minified to `true`. This option will apply a filter to create task on variant name matches with regex.

  for example,
  - `(.*)Release` matches variants name ends with "Release"
  - `Minified(*.)` matches variants name start with "Minified"

<br/>
<br/>

## Obfuscation strategy

Plugin provides obfuscation strategy to generate dictionary
- `ObfuscationStrategy.RANDOM_CHARACTERS` - Random single unicode characters. This option in default.
- `ObfuscationStrategy.RANDOM_WORDS` - Random word contains 5 unicode characters.
- `ObfuscationStrategy.RANDOM` - Random pick strategy on each build.
