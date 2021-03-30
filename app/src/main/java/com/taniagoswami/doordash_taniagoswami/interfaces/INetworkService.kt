package com.taniagoswami.doordash_taniagoswami.interfaces

import okhttp3.Callback

internal interface INetworkService {
    fun request(urlString: String, callback: Callback)
    fun request(urlString: String, params: Array<Pair<String, String>>, callback: Callback)
}