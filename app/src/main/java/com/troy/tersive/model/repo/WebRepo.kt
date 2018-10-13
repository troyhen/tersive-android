package com.troy.tersive.model.repo

import androidx.annotation.WorkerThread
import com.troy.tersive.model.data.WebDoc
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@WorkerThread
class WebRepo @Inject constructor() {

    private var client = OkHttpClient()

    fun load(webDoc: WebDoc): CharSequence? {
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

    fun load(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        return response.body()?.string()
    }
}
