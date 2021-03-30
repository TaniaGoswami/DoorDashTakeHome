package com.taniagoswami.doordash_taniagoswami.models

import com.google.gson.annotations.SerializedName

data class RestaurantsResponse(@SerializedName(value = "num_results") val results: Int, @SerializedName(value = "next_offset") val nextOffset: Int, @SerializedName(value = "stores") val restaurants: Array<Restaurant>)