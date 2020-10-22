package com.troy.tersive.mgr

import timber.log.Timber
import java.time.Instant
import java.time.format.DateTimeParseException

class InstantTransformer : PreferenceTransformer<Instant>() {

    override fun encode(value: Instant?) = value?.toString()

    override fun decode(value: String?) = try {
        value?.let { Instant.parse(it) }
    } catch (e: DateTimeParseException) {
        Timber.e(e)
        null
    }
}