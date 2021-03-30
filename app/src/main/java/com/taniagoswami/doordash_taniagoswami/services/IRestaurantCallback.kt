package com.taniagoswami.doordash_taniagoswami.services

import com.taniagoswami.doordash_taniagoswami.models.RestaurantDetail

interface IRestaurantCallback {
    fun receivedRestaurant(restaurant: RestaurantDetail?)
    fun receivedFailure(message: String)
}