package com.taniagoswami.doordash_taniagoswami.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.taniagoswami.doordash_taniagoswami.DoorDashApplication
import com.taniagoswami.doordash_taniagoswami.models.RestaurantDetail
import com.taniagoswami.doordash_taniagoswami.services.IRestaurantCallback

class RestaurantDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val restaurantService = (application as? DoorDashApplication)?.restaurantService
    val errorMessage = MutableLiveData("")

    private val restaurant = MutableLiveData<RestaurantDetail?>()

    fun getRestaurant(): MutableLiveData<RestaurantDetail?> {
        return restaurant
    }

    fun loadRestaurant(id: String) {
        val callback = object : IRestaurantCallback {
            override fun receivedRestaurant(restaurant: RestaurantDetail?) {
                this@RestaurantDetailViewModel.restaurant.postValue(restaurant)
            }

            override fun receivedFailure(message: String) {
                errorMessage.postValue(message)
            }
        }
        restaurantService?.loadRestaurant(id, callback)
    }
}