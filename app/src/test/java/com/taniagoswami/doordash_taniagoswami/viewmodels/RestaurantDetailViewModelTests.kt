package com.taniagoswami.doordash_taniagoswami.viewmodels

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.taniagoswami.doordash_taniagoswami.DoorDashApplication
import com.taniagoswami.doordash_taniagoswami.models.Restaurant
import com.taniagoswami.doordash_taniagoswami.models.RestaurantDetail
import com.taniagoswami.doordash_taniagoswami.services.IRestaurantCallback
import com.taniagoswami.doordash_taniagoswami.services.IRestaurantsCallback
import com.taniagoswami.doordash_taniagoswami.services.RestaurantService
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Test

class RestaurantDetailViewModelTests : TestCase() {
    private val mockApplication = mockk<DoorDashApplication>()
    private val mockRestaurantService = mockk<RestaurantService>(relaxed = true)

    public override fun setUp() {
        super.setUp()
        every { mockApplication.restaurantService }.returns(mockRestaurantService)
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
    }

    public override fun tearDown() {
        super.tearDown()
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @Test
    fun testInitialState() {
        val restaurantDetailViewModel = RestaurantDetailViewModel(mockApplication)
        assertEquals(null, restaurantDetailViewModel.getRestaurant().value)
        assertEquals("", restaurantDetailViewModel.errorMessage.value)
    }

    @Test
    fun testErrorSet() {
        val restaurantDetailViewModel = RestaurantDetailViewModel(mockApplication)
        every { mockRestaurantService.loadRestaurant(any(), any()) } answers { secondArg<IRestaurantCallback>().receivedFailure("error message") }
        restaurantDetailViewModel.loadRestaurant("")
        assertEquals("error message", restaurantDetailViewModel.errorMessage.value)
    }

    @Test
    fun testRestaurantsSet() {
        val restaurantDetailViewModel = RestaurantDetailViewModel(mockApplication)
        val restaurant = RestaurantDetail("1", "desc", "url", "s", "p", 0.0)
        every { mockRestaurantService.loadRestaurant(any(), any()) } answers { secondArg<IRestaurantCallback>().receivedRestaurant(restaurant) }
        restaurantDetailViewModel.loadRestaurant("")
        assertEquals("1", restaurantDetailViewModel.getRestaurant().value?.name)
    }
}