package com.taniagoswami.doordash_taniagoswami.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.taniagoswami.doordash_taniagoswami.R
import com.taniagoswami.doordash_taniagoswami.constants.IntentConstants
import com.taniagoswami.doordash_taniagoswami.models.RestaurantDetail
import com.taniagoswami.doordash_taniagoswami.viewmodels.RestaurantDetailViewModel

class RestaurantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        val restaurantId = intent.getStringExtra(IntentConstants.RESTAURANT_ID) ?: run { finish() }

        val restaurantDetailViewModel by viewModels<RestaurantDetailViewModel>()

        restaurantDetailViewModel.getRestaurant().observe(this, Observer { restaurant ->
            restaurant ?: return@Observer
            populateUI(restaurant)
        })

        restaurantDetailViewModel.errorMessage.observe(this, Observer { message ->
            if (message.isEmpty()) return@Observer
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle(R.string.error_title)
            alertDialog.setMessage(message)
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            alertDialog.show()
        })

        restaurantDetailViewModel.loadRestaurant(restaurantId as String)
    }

    private fun populateUI(restaurantDetail: RestaurantDetail? = null) {
        restaurantDetail ?: return
        title = restaurantDetail.name
        Glide.with(this)
            .load(restaurantDetail.thumbnailsUrl)
            .fitCenter()
            .into(findViewById(R.id.restaurant_thumbnail))
        findViewById<TextView>(R.id.restaurant_description)?.text = restaurantDetail.description
        findViewById<TextView>(R.id.restaurant_status)?.text = restaurantDetail.status
        findViewById<TextView>(R.id.rating)?.text = "${restaurantDetail.rating}"
    }
}