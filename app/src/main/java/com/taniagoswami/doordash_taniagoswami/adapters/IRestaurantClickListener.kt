package com.taniagoswami.doordash_taniagoswami.adapters

import com.taniagoswami.doordash_taniagoswami.models.Restaurant

interface IRestaurantClickListener {
    fun restaurantClicked(restaurant: Restaurant)
}