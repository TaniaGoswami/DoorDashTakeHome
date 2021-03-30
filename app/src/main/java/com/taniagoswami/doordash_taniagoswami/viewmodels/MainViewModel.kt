package com.taniagoswami.doordash_taniagoswami.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.taniagoswami.doordash_taniagoswami.DoorDashApplication
import com.taniagoswami.doordash_taniagoswami.models.Restaurant
import com.taniagoswami.doordash_taniagoswami.services.IRestaurantsCallback

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val restaurantService = (application as? DoorDashApplication)?.restaurantService
    val errorMessage = MutableLiveData("")

    private val restaurantsList: MutableLiveData<Array<Restaurant>> by lazy {
        MutableLiveData<Array<Restaurant>>().also {
            loadRestaurants(0)
        }
    }

    fun getRestaurants(): MutableLiveData<Array<Restaurant>> {
        return restaurantsList
    }

    fun loadRestaurants(offset: Int? = null) {
        val callback = object : IRestaurantsCallback {
            override fun receivedRestaurants(restaurants: Array<Restaurant>) {
                val allRestaurants = (restaurantsList.value ?: arrayOf()).plus(restaurants)
                restaurantsList.postValue(allRestaurants)
            }

            override fun receivedFailure(message: String) {
                errorMessage.postValue(message)
            }
        }
        restaurantService?.loadRestaurants(callback, offset)
    }
}