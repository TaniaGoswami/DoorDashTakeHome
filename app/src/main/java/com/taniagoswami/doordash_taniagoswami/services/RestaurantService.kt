package com.taniagoswami.doordash_taniagoswami.services

import com.google.gson.GsonBuilder
import com.taniagoswami.doordash_taniagoswami.constants.ApiConstants
import com.taniagoswami.doordash_taniagoswami.models.RestaurantDetail
import com.taniagoswami.doordash_taniagoswami.models.RestaurantsResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.Exception

class RestaurantService {
    private val networkService = NetworkService()
    private var offset = 0
    private var numResults = Integer.MAX_VALUE

    fun loadRestaurants(restaurantsCallback: IRestaurantsCallback, requiredOffset: Int? = null) {
        requiredOffset?.let { offset = it }

        val callback = object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                restaurantsCallback.receivedFailure(e.localizedMessage ?: "")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val restaurantsResponse = GsonBuilder().create()
                        .fromJson<RestaurantsResponse>(response.body?.string(), RestaurantsResponse::class.java)
                    offset = restaurantsResponse.nextOffset
                    numResults = restaurantsResponse.results
                    restaurantsCallback.receivedRestaurants(restaurantsResponse.restaurants)
                } catch (e: Exception) {
                    restaurantsCallback.receivedFailure("Unable to load data")
                }
            }
        }

        val params = arrayListOf<Pair<String, String>>()
        params.add(Pair(ApiConstants.LATITUDE, ApiConstants.LAT_COORDINATE))
        params.add(Pair(ApiConstants.LONGITUDE, ApiConstants.LNG_COORDINATE))
        params.add(Pair(ApiConstants.OFFSET, offset.toString()))
        params.add(Pair(ApiConstants.LIMIT, ApiConstants.LIMIT_VALUE))
        networkService.request(ApiConstants.RESTAURANTS_URL, params.toTypedArray(), callback)
    }

    fun loadRestaurant(id: String, restaurantCallback: IRestaurantCallback) {
        val callback = object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                restaurantCallback.receivedFailure(e.localizedMessage ?: "")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val restaurantResponse = GsonBuilder().create()
                        .fromJson<RestaurantDetail>(response.body?.string(), RestaurantDetail::class.java)
                    restaurantCallback.receivedRestaurant(restaurantResponse)
                } catch (e: Exception) {
                    restaurantCallback.receivedFailure("Unable to load data")
                }
            }
        }
        networkService.request(ApiConstants.RESTAURANT_URL + id, callback)
    }
}