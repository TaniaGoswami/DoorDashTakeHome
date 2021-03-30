package com.taniagoswami.doordash_taniagoswami.viewmodels

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.taniagoswami.doordash_taniagoswami.DoorDashApplication
import com.taniagoswami.doordash_taniagoswami.models.Restaurant
import com.taniagoswami.doordash_taniagoswami.services.IRestaurantsCallback
import com.taniagoswami.doordash_taniagoswami.services.RestaurantService
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Test

class MainViewModelTests : TestCase() {
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
        val mainViewModel = MainViewModel(mockApplication)
        assertEquals(null, mainViewModel.getRestaurants().value?.size)
        assertEquals("", mainViewModel.errorMessage.value)
    }

    @Test
    fun testErrorSet() {
        val mainViewModel = MainViewModel(mockApplication)
        every { mockRestaurantService.loadRestaurants(any()) } answers { firstArg<IRestaurantsCallback>().receivedFailure("error message") }
        mainViewModel.loadRestaurants()
        assertEquals("error message", mainViewModel.errorMessage.value)
    }

    @Test
    fun testRestaurantsSet() {
        val mainViewModel = MainViewModel(mockApplication)
        val restaurants = arrayOf(Restaurant("1", "Chipotle", "desc", "url", null))
        every { mockRestaurantService.loadRestaurants(any()) } answers { firstArg<IRestaurantsCallback>().receivedRestaurants(restaurants) }
        mainViewModel.loadRestaurants()
        assertEquals("1", mainViewModel.getRestaurants().value?.firstOrNull()?.id)
        assertEquals(1, mainViewModel.getRestaurants().value?.size)
    }

    @Test
    fun testRestaurantListGrows() {
        val mainViewModel = MainViewModel(mockApplication)
        val restaurants = arrayOf(Restaurant("1", "Chipotle", "desc", "url", null))
        every { mockRestaurantService.loadRestaurants(any()) } answers { firstArg<IRestaurantsCallback>().receivedRestaurants(restaurants) }
        mainViewModel.loadRestaurants()
        assertEquals("1", mainViewModel.getRestaurants().value?.firstOrNull()?.id)
        assertEquals(1, mainViewModel.getRestaurants().value?.size)
        val restaurants2 = arrayOf(Restaurant("2", "McDonalds", "desc", "url", null))
        every { mockRestaurantService.loadRestaurants(any()) } answers { firstArg<IRestaurantsCallback>().receivedRestaurants(restaurants2) }
        mainViewModel.loadRestaurants()
        assertEquals("1", mainViewModel.getRestaurants().value?.firstOrNull()?.id)
        assertEquals("2", mainViewModel.getRestaurants().value?.get(1)?.id)
        assertEquals(2, mainViewModel.getRestaurants().value?.size)
    }
}
