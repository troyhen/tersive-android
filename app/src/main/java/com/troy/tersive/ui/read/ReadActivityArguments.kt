package com.troy.tersive.ui.read

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.troy.tersive.model.data.WebDoc

data class ReadActivityArguments(
    val webDoc: WebDoc
) {
    fun toBundle(): Bundle {
        return Bundle().apply {
            putParcelable(WEB_DOC, webDoc)
        }
    }

    fun toIntent(context: Context) = Intent(context, ReadActivity::class.java).apply {
        putExtra(WEB_DOC, webDoc)
    }

    companion object {
        private const val WEB_DOC = "webDoc"

        fun fromBundle(bundle: Bundle): ReadActivityArguments? {
            val webDoc: WebDoc = bundle.getParcelable<WebDoc>(WEB_DOC) ?: return null
            return ReadActivityArguments(webDoc)
        }

        fun fromIntent(intent: Intent): ReadActivityArguments? {
            val webDoc: WebDoc = intent.getParcelableExtra<WebDoc>(WEB_DOC) ?: return null
            return ReadActivityArguments(webDoc)
        }
    }
}