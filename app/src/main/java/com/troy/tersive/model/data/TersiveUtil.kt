package com.troy.tersive.model.data

import timber.log.Timber
import javax.inject.Inject

class TersiveUtil @Inject constructor() {

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
}
