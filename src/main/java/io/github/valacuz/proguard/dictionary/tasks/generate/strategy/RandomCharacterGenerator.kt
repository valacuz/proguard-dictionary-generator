package io.github.valacuz.proguard.dictionary.tasks.generate.strategy

/**
 * This obfuscation dictionary contains single unicode character.
 */
open class RandomCharacterGenerator : Generator() {

    val startUnicodeRange: IntRange
        get() = (256..512)

    val endUnicodeRange: IntRange
        get() = (14336..16384)


    override fun create(): List<String> {
        val start = startUnicodeRange.random()
        val end = start + endUnicodeRange.random()

        return (start..end)
            .filter { Character.isValidCodePoint(it) && Character.isJavaIdentifierPart(it) }
            .map { it.stringByUnicode() }
    }

    private fun Int.stringByUnicode(): String = String(Character.toChars(this))
}