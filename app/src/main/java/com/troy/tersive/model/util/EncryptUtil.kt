package com.troy.tersive.model.util

import android.annotation.SuppressLint
import timber.log.Timber
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptUtil @Inject
constructor() {

    /**
     * Encrypt the plain text password.
     *
     * @param plainKey The password.
     * @return The encrypted password String or null if it failed to encrypt
     */
    fun encrypt(plainKey: String?): String? {
        if (plainKey == null) {
            return null
        }
        try {
            val input = plainKey.toByteArray(charset(UTF8))
            val key = SecretKeySpec(KEY_BYTES, ALGORITHM)

            @SuppressLint("GetInstance") // Don't want to mess with changing this (been working for years)
            val cipher = Cipher.getInstance(ALGORITHM)

            // encryption pass
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val cipherText = ByteArray(cipher.getOutputSize(input.size))
            val ctLength = cipher.update(input, 0, input.size, cipherText, 0)
            cipher.doFinal(cipherText, ctLength)
            return encode(cipherText)
        } catch (e: Exception) {
            Timber.e(e, "encrypt failure")
        }

        return null
    }

    /**
     * Encrypt the plain text password.
     *
     * @param encryptedKey The encrypted password.
     * @return The decrypted plain-text password String or null if it failed to decrypt
     */
    fun decrypt(encryptedKey: String?): String? {
        if (encryptedKey == null || encryptedKey.isEmpty()) {
            return null
        }
        try {
            val key = SecretKeySpec(KEY_BYTES, ALGORITHM)

            @SuppressLint("GetInstance") // Don't want to mess with changing this (been working for years)
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key)
            val decoded = decode(encryptedKey)
            val plainText = ByteArray(cipher.getOutputSize(decoded.size))
            var ptLength = cipher.update(decoded, 0, decoded.size, plainText, 0)
            ptLength += cipher.doFinal(plainText, ptLength)
            return String(plainText).substring(0, ptLength)
        } catch (e: Exception) {
            Timber.e(e, "decrypt failure")
        }

        return null
    }

    private fun encode(bytes: ByteArray): String {
        val builder = StringBuilder()

        for (aByte in bytes) {
            builder.append(HEX_CHARS[(aByte.toInt() shr BITS_IN_HALF_BYTE and MASK.toInt())])
            builder.append(HEX_CHARS[aByte.toInt() and MASK.toInt()])
        }
        return builder.toString()
    }

    private fun decode(str: String): ByteArray {
        val length = str.length / 2
        val decoded = ByteArray(length)
        val chars = str.toCharArray()
        var index = 0
        var i = 0
        while (i < chars.size) {
            val id1 = indexOf(HEX_CHARS, chars[i])
            if (id1 == -1) {
                throw IllegalArgumentException("Character " + chars[i] + " at position " + i + " is not a valid hexidecimal character")
            }
            val id2 = indexOf(HEX_CHARS, chars[++i])
            if (id2 == -1) {
                throw IllegalArgumentException("Character " + chars[i] + " at position " + i + " is not a valid hexidecimal character")
            }
            decoded[index++] = (id1 shl BITS_IN_HALF_BYTE or id2).toByte()
            ++i
        }
        return decoded
    }

    private fun indexOf(array: CharArray?, valueToFind: Char): Int {
        if (array == null) {
            return INDEX_NOT_FOUND
        }
        for (i in array.indices) {
            if (valueToFind == array[i]) {
                return i
            }
        }
        return INDEX_NOT_FOUND
    }

    companion object {
        private const val UTF8 = "utf-8"
        const val ALGORITHM = "AES"
        private val HEX_CHARS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        private val KEY_BYTES = byteArrayOf(0x76, 0x7F, 0x3F, 0x00, 0x42, 0x21, 0x24, 0x48, 0x01, 0x2D, 0x3D, 0x4D, 0x22, 0x1A, 0x36, 0x18)
        private const val INDEX_NOT_FOUND = -1
        private const val BITS_IN_HALF_BYTE = 4
        private const val MASK: Char = 0x0f.toChar()
    }
}
