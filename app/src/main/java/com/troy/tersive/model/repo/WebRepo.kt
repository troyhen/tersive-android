package com.troy.tersive.model.repo

import okhttp3.OkHttpClient
import okhttp3.Request

class WebRepo {

    private var client = OkHttpClient()

    fun load(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        return response.body()?.string()
    }
}
