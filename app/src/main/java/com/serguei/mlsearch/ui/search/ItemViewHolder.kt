package com.serguei.mlsearch.ui.search

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.serguei.mlsearch.R
import com.serguei.mlsearch.model.Item
import com.squareup.picasso.Picasso

class ItemViewHolder(view: View, adapter: ItemsAdapter) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.item_title)
    private val price: TextView = view.findViewById(R.id.item_price)
    private val thumbnail: ImageView = view.findViewById(R.id.item_thumbnail)
    private val freeShipping: ImageView = view.findViewById(R.id.item_free_shipping_icon)
    private val rating: RatingBar = view.findViewById(R.id.item_rating)

    private var item: Item? = null

    init {
        view.setOnClickListener {
            item?.let {
                adapter.onItemPressed(it)
            }
        }
    }

    fun bind(item: Item?) {
        if (item != null) {
            showItemData(item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showItemData(item: Item) {
        this.item = item
        title.text = item.title
        price.text = "$ ${item.price.toInt()}"
        freeShipping.visibility = if(item.freeShipping) View.VISIBLE else View.GONE
        rating.rating = item.ratingAverage.toFloat()
        // Had crashes with null thumbnails from the API, TODO: set default placeholder image
        val imageUrl = if (!TextUtils.isEmpty(item.thumbnail)) item.thumbnail else "http://via.placeholder.com/100x100"
        Picasso.get().load(imageUrl).into(thumbnail)
    }

    companion object {
        fun create(parent: ViewGroup, adapter: ItemsAdapter): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.search_list_item, parent, false)
            return ItemViewHolder(view, adapter)
        }
    }
}