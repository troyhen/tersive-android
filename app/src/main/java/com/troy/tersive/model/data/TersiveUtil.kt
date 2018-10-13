package com.troy.tersive.model.data

import com.troy.tersive.model.db.tersive.Tersive
import timber.log.Timber
import javax.inject.Inject

class TersiveUtil @Inject constructor() {

    private val map = HashMap<CharSequence, String>()
    private val output = StringBuilder()
    private val word = StringBuilder()
    private lateinit var text: CharSequence
    private lateinit var words: Array<CharSequence>
    private var index = 0
    private var end = 0
    private val ignored = hashSetOf("the")

    fun loadPhrases(phrases: List<Tersive>, key: Boolean) {
        map.clear()
        phrases.forEach {
            (if (key) it.kbd else it.lvl4)?.let { tersive ->
                map[it.phrase] = tersive
            }
        }
    }

    fun toTersive(text: CharSequence): CharSequence {
//        val words = toWords(text)
        this.text = text
        output.setLength(0)
        index = 0
        end = text.length
        words = arrayOf("", "", "", "", "")

        var needed = words.size
        while (index < end) {
            fillWords(needed)

            val phrase2 = "${words[0]} ${words[1]}"
            val phrase3 = "$phrase2 ${words[2]}"
            val phrase4 = "$phrase3 ${words[3]}"
            val phrase5 = "$phrase4 ${words[4]}"

            val tersive1 = map[words[0]]
            val tersive2 = map[phrase2]
            val tersive3 = map[phrase3]
            val tersive4 = map[phrase4]
            val tersive5 = map[phrase5]

            when {
                tersive5 != null -> {
                    append(tersive5)
                    needed = 5
                }
                tersive4 != null -> {
                    append(tersive4)
                    needed = 4
                }
                tersive3 != null -> {
                    append(tersive3)
                    needed = 3
                }
                tersive2 != null -> {
                    append(tersive2)
                    needed = 2
                }
                tersive1 != null -> {
                    append(tersive1)
                    needed = 1
                }
                ignored.contains(words[0]) -> {
                }
                else -> {
                    Timber.i("Unknown word '${words[0]}'")
                    needed = 1
                }
            }
        }
        return output
    }

    private fun append(phrase: CharSequence) {
        if (output.isNotEmpty()) output.append(' ')
        output.append(phrase)
    }

    private fun fillWords(needed: Int) {
        val last = words.size - 1
        for (ix in 0.until(needed)) {
            word.setLength(0)
            loop@ while (index < text.length) {
                val found = word.isNotEmpty()
                val ch = text[index++].toLowerCase()
                val isLetter = ch.isLetterOrDigit()
                val isSpace = ch.isWhitespace()
                when {
                    found && !isLetter -> break@loop
                    found || !isSpace -> word.append(ch)
                }
            }
            System.arraycopy(words, 1, words, 0, last)
            words[last] = word.toString()
        }
    }

//    private fun toWords(text: CharSequence): List<CharSequence> {
//        var start = 0
//        var state = LexState.BETWEEN
//        val words = mutableListOf<CharSequence>()
//        text.forEachIndexed { index, ch ->
//            val c = ch.toLowerCase()
//            if (state == LexState.PUNCTUATION) {
//                words.add(text.subSequence(start, index))
//                state = LexState.BETWEEN
//            }
//            when (state) {
//                LexState.WITHIN -> when {
//                    (c == '\u0027' || c == '\u2019') && index < text.length - 1 && text[index + 1].isLetterOrDigit() -> { // handle contraction
//                    }
//                    c.isWhitespace() || !c.isLetterOrDigit() -> {
//                        words.add(text.subSequence(start, index))
//                        state = LexState.BETWEEN
//                    }
//                }
//                else -> {
//                    when {
//                        c.isLetterOrDigit() -> {
//                            start = index
//                            state = LexState.WITHIN
//                        }
//                        !c.isWhitespace() -> {
//                            start = index
//                            state = LexState.PUNCTUATION
//                        }
//                        else -> words.add(text.subSequence(start, index + 1))
//                    }
//                }
//            }
//        }
//        return words
//    }

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
