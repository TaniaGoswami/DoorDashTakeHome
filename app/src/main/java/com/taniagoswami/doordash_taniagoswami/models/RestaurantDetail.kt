package com.taniagoswami.doordash_taniagoswami.models

import com.google.gson.annotations.SerializedName

data class RestaurantDetail(
    val name: String,
    val description: String,
    @SerializedName(value = "cover_img_url")
    val thumbnailsUrl: String,
    val status: String,
    @SerializedName(value = "phone_number")
    val phoneNumber: String,
    @SerializedName(value = "average_rating")
    val rating: Double
)