package com.troy.tersive.model.data

import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.experimental.and

@Singleton
class HashUtil @Inject constructor() {

    fun hash(string: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(string.toByteArray())
            val bytes = md.digest()
            val sb = StringBuilder()
            for (byte in bytes) {
                sb.append(Integer.toString(byte.and(BYTE) + 0x100, 16).substring(1))
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            Timber.e(e)
        }
        return ""
    }

    companion object {
        const val BYTE = 0xff.toByte()
    }
}
