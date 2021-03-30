package com.taniagoswami.doordash_taniagoswami.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.taniagoswami.doordash_taniagoswami.R
import com.taniagoswami.doordash_taniagoswami.models.Restaurant

class RestaurantsListAdapter(private val itemClickListener: IRestaurantClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var restaurants = arrayOf<Restaurant>()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val restaurantNameTv = view.findViewById<TextView>(R.id.restaurant_tv)
        private val restaurantDescriptionTv = view.findViewById<TextView>(R.id.restaurant_description_tv)
        private val restaurantThumnailImageView = view.findViewById<ImageView>(R.id.restaurant_thumbnail)
        private val restaurantStatus = view.findViewById<TextView>(R.id.restaurant_status_tv)

        fun bind(restaurant: Restaurant, itemClickListener: IRestaurantClickListener) {
            val context = itemView.context
            restaurantNameTv.text = restaurant.name
            restaurantDescriptionTv.text = restaurant.description
            val deliveryMinutes = restaurant.status?.deliveryMinutes?.firstOrNull()
            restaurantStatus.text = if (deliveryMinutes != null) context.getString(R.string.delivery_minutes, deliveryMinutes) else context.getString(R.string.closed)
            Glide.with(itemView)
                .load(restaurant.thumbnailsUrl)
                .centerCrop()
                .into(restaurantThumnailImageView)
            itemView.setOnClickListener { itemClickListener.restaurantClicked(restaurant) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurants.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder = holder as? ViewHolder ?: return
        holder.bind(restaurants[position], itemClickListener)
    }

    fun updateData(restaurants: Array<Restaurant>) {
        this.restaurants = restaurants
    }
}