package com.taniagoswami.doordash_taniagoswami.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName(value = "cover_img_url")
    val thumbnailsUrl: String,
    val status: Status?
): Serializable