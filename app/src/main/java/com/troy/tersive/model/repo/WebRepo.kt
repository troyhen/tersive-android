package com.troy.tersive.model.repo

import com.troy.tersive.model.data.WebDoc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class WebRepo {

    private var client = OkHttpClient()

    suspend fun load(webDoc: WebDoc): CharSequence? {
        return load(webDoc.url)?.let { content ->
            val start = webDoc.beginning?.let {
                val index = content.indexOf(it)
                if (index >= 0) index else 0
            } ?: 0
            val end = webDoc.ending?.let {
                val last = content.lastIndexOf(it)
                if (last >= 0) last + it.length else content.length
            } ?: content.length
            content.subSequence(start, end)
        }
    }

    suspend fun load(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .build()
        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            response.body?.string()
        }
    }
}
