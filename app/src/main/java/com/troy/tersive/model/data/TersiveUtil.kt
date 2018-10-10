package com.troy.tersive.model.data

import com.troy.tersive.model.db.tersive.Tersive
import timber.log.Timber
import javax.inject.Inject

class TersiveUtil @Inject constructor() {

    private val map = HashMap<String, String>()
    private val buf = StringBuilder()

    fun loadPhrases(phrases: List<Tersive>, key: Boolean) {
        map.clear()
        phrases.forEach {
            (if (key) it.kbd else it.lvl4)?.let { tersive ->
                map[it.phrase] = tersive
            }
        }
    }

    fun toTersive(text: CharSequence): CharSequence {
        val words = toWords(text)
        buf.setLength(0)
        var index = 0
        val end = words.size
        while (index < end) {
            val word0 = words[index + 0]
            val word1 = if (index + 1 < end) words[index + 1] else ""
            val word2 = if (index + 2 < end) words[index + 2] else ""
            val word3 = if (index + 3 < end) words[index + 3] else ""
            val word4 = if (index + 4 < end) words[index + 4] else ""

            val phrase2 = "$word0 $word1"
            val phrase3 = "$phrase2 $word2"
            val phrase4 = "$phrase3 $word3"
            val phrase5 = "$phrase4 $word4"

            val tersive5 = map[phrase5]
            val tersive4 = map[phrase4]
            val tersive3 = map[phrase3]
            val tersive2 = map[phrase2]
            val tersive1 = map[word0]

            when {
                tersive5 != null -> {
                    if (buf.isNotEmpty()) buf.append(' ')
                    buf.append(tersive5)
                    index += 5
                }
                tersive4 != null -> {
                    if (buf.isNotEmpty()) buf.append(' ')
                    buf.append(tersive4)
                    index += 4
                }
                tersive3 != null -> {
                    if (buf.isNotEmpty()) buf.append(' ')
                    buf.append(tersive3)
                    index += 3
                }
                tersive2 != null -> {
                    if (buf.isNotEmpty()) buf.append(' ')
                    buf.append(tersive2)
                    index += 2
                }
                tersive1 != null -> {
                    if (buf.isNotEmpty()) buf.append(' ')
                    buf.append(tersive1)
                    index++
                }
            }
        }
        return buf
    }

    private fun toWords(text: CharSequence): List<CharSequence> {
        var start = 0
        var state = LexState.BETWEEN
        val words = mutableListOf<CharSequence>()
        text.forEachIndexed { index, ch ->
            val c = ch.toLowerCase()
            if (state == LexState.PUNCTUATION) {
                words.add(text.subSequence(start, index))
                state = LexState.BETWEEN
            }
            when (state) {
                LexState.WITHIN -> when {
                    (c == '\u0027' || c == '\u2019') && index < text.length - 1 && text[index + 1].isLetterOrDigit() -> { // handle contraction
                    }
                    c.isWhitespace() || !c.isLetterOrDigit() -> {
                        words.add(text.subSequence(start, index))
                        state = LexState.BETWEEN
                    }
                }
                else -> {
                    when {
                        c.isLetterOrDigit() -> {
                            start = index
                            state = LexState.WITHIN
                        }
                        !c.isWhitespace() -> {
                            start = index
                            state = LexState.PUNCTUATION
                        }
                        else -> words.add(text.subSequence(start, index + 1))
                    }
                }
            }
        }
        return words
    }

    fun optimizeHand(tersive: String): CharSequence {
        val buffer = StringBuilder(" ")
        var index = 0
        val length = tersive.length
        var prev = ' '

        while (index < length) {
            val char = tersive[index++]
            val next = if (index < length) tersive[index] else ' '
            buffer.append(fix(prev, char, next))
            prev = char
        }
        buffer.append(' ')
        Timber.d("Converted '$tersive' to '$buffer'")
        return buffer
    }

    private fun fix(prev: Char, char: Char, next: Char): Char {
        return when {
            prev.isWhitespace() -> {
                when (char) {
                    'b' -> 'B'
                    'p' -> 'P'
                    'v' -> 'V'
                    'f' -> 'F'
                    'g' -> 'G'
                    'k' -> 'K'
                    'c' -> 'C'
                    'j' -> 'J'
                    'z' -> 'Z'
                    's' -> '{'
                    'd' -> 'D'
                    't' -> '}'
                    else -> char
                }
            }
            next.isWhitespace() -> {
                when (char) {
                    'b' -> '!'
                    'p' -> '@'
                    'v' -> '#'
                    'f' -> '$'
                    'g' -> '&'
                    'k' -> '*'
                    'c' -> '^'
                    'j' -> '%'
                    'z' -> '\\'
                    's' -> '['
                    'd' -> '\''
                    't' -> ']'
                    'K' -> '>'
                    'N' -> '<'
                    else -> char
                }
            }
            else -> char
        }
    }

    enum class LexState {
        BETWEEN, WITHIN, PUNCTUATION
    }
}
