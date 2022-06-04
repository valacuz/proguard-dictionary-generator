package io.github.valacuz.proguard.dictionary.tasks.generate.strategy

/**
 * This obfuscation dictionary contains random word from unicode character.
 */
class RandomWordGenerator(private val dictionarySize: Int = 40_000) : Generator() {

    private val randomCharacterGenerator = RandomCharacterGenerator()

    override fun create(): List<String> {
        val start = randomCharacterGenerator.startUnicodeRange.random()
        val end = start + randomCharacterGenerator.endUnicodeRange.random()
        val choices = start..end

        return (0..dictionarySize)
            .map { randomWord(choices) }
            .distinct()
    }

    private fun randomWord(pool: IntRange, length: Int = 5): String {
        val builder = StringBuilder(length)
        for (i in 0 until length) {
            builder.append(pool.random().stringByUnicode())
        }
        return builder.toString()
    }

    private fun Int.stringByUnicode(): String = String(Character.toChars(this))
}