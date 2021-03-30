package com.taniagoswami.doordash_taniagoswami.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taniagoswami.doordash_taniagoswami.R
import com.taniagoswami.doordash_taniagoswami.adapters.IRestaurantClickListener
import com.taniagoswami.doordash_taniagoswami.adapters.RestaurantsListAdapter
import com.taniagoswami.doordash_taniagoswami.constants.IntentConstants
import com.taniagoswami.doordash_taniagoswami.models.Restaurant
import com.taniagoswami.doordash_taniagoswami.viewmodels.MainViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.restaurants_list)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val restaurantClickedListener = object: IRestaurantClickListener {
            override fun restaurantClicked(restaurant: Restaurant) {
                val intent = Intent(this@MainActivity, RestaurantDetailActivity::class.java)
                intent.putExtra(IntentConstants.RESTAURANT_ID, restaurant.id)
                startActivity(intent)
            }
        }

        val adapter = RestaurantsListAdapter(restaurantClickedListener)
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        val mainViewModel by viewModels<MainViewModel>()

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    mainViewModel.loadRestaurants()
                }
            }
        })

        mainViewModel.getRestaurants().observe(this, Observer { restaurants ->
            adapter.updateData(restaurants)
            adapter.notifyDataSetChanged();
        })

        mainViewModel.errorMessage.observe(this, Observer { message ->
            if (message.isEmpty()) return@Observer
            val alertDialog = AlertDialog.Builder(this@MainActivity).create()
            alertDialog.setTitle(R.string.error_title)
            alertDialog.setMessage(message)
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            alertDialog.show()
        })

        mainViewModel.loadRestaurants(0)
    }
}
