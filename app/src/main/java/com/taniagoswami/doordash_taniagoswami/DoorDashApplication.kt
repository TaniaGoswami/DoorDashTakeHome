package com.taniagoswami.doordash_taniagoswami

import android.app.Application
import com.taniagoswami.doordash_taniagoswami.services.RestaurantService

class DoorDashApplication: Application() {
    val restaurantService = RestaurantService()
}