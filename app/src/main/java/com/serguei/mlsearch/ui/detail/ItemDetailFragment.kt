package com.serguei.mlsearch.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serguei.mlsearch.R
import com.serguei.mlsearch.api.MercadoLibreService
import com.serguei.mlsearch.api.getItemDescription
import com.serguei.mlsearch.model.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_detail.*

class ItemDetailFragment : Fragment() {

    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = it.getSerializable(ARG_ITEM_ID) as Item
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Populate the views with the item information
        item_title.text = item.title
        item_price.text = "$ ${item.price.toInt()}"
        item_rating.rating = item.ratingAverage.toFloat()
        item_free_shipping.visibility = if (item.freeShipping) View.VISIBLE else View.GONE

        // Had crashes with null thumbnails from the API, TODO: set default placeholder image
        val imageUrl = if (!TextUtils.isEmpty(item.thumbnail)) item.thumbnail else "http://via.placeholder.com/100x100"
        Picasso.get().load(imageUrl).into(item_image)

        // Get the item description
        fetchDescription(item.id)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_detail, container, false)
    }


    private fun fetchDescription(itemId: String) {
        item_detail_loading.visibility = View.VISIBLE
        item_description.visibility = View.GONE

        getItemDescription(MercadoLibreService.create(), itemId, { description ->
            item_description?.visibility = View.VISIBLE
            item_detail_loading?.visibility = View.GONE
            item_description?.text = description.text
        }, { _ ->
            item_description?.visibility = View.GONE
            item_detail_loading?.visibility = View.GONE
        })
    }

    companion object {
        const val ARG_ITEM_ID = "item_selected"
    }
}
