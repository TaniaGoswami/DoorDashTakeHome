package com.taniagoswami.doordash_taniagoswami.services

import com.taniagoswami.doordash_taniagoswami.models.Restaurant

interface IRestaurantsCallback {
    fun receivedRestaurants(restaurants: Array<Restaurant>)
    fun receivedFailure(message: String)
}