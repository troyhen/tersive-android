package com.troy.tersive.model.repo

import androidx.annotation.WorkerThread
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@WorkerThread
class WebRepo @Inject constructor() {

    private var client = OkHttpClient()

    fun load(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        return response.body()?.string()
    }
}
