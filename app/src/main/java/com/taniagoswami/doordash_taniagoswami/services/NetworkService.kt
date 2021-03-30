package com.taniagoswami.doordash_taniagoswami.services

import com.taniagoswami.doordash_taniagoswami.interfaces.INetworkService
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkService: INetworkService {
    private val client = OkHttpClient()

    override fun request(urlString: String, callback: Callback) {
        val request = Request.Builder()
            .url(urlString)
            .build()

        client.newCall(request).enqueue(callback)
    }

    override fun request(urlString: String, params: Array<Pair<String, String>>, callback: Callback) {
        val urlBuilder = urlString.toHttpUrlOrNull()?.newBuilder() ?: return
        for (i in params.indices) {
            urlBuilder.addQueryParameter(params[i].first, params[i].second)
        }
        val url = urlBuilder.build().toString()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(callback)
    }
}