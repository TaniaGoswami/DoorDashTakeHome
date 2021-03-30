package com.taniagoswami.doordash_taniagoswami.models

import com.google.gson.annotations.SerializedName

data class Status(@SerializedName(value = "asap_minutes_range") val deliveryMinutes: Array<Int>?)