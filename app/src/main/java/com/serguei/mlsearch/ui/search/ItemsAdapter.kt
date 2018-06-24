package com.serguei.mlsearch.ui.search

import android.arch.paging.PagedListAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.serguei.mlsearch.R
import com.serguei.mlsearch.model.Item
import com.serguei.mlsearch.ui.detail.ItemDetailActivity
import com.serguei.mlsearch.ui.detail.ItemDetailFragment
import kotlinx.android.synthetic.main.item_list.*

class ItemsAdapter(private val twoPane: Boolean, private val parentActivity: SearchItemsActivity) : PagedListAdapter<Item, RecyclerView.ViewHolder>(ITEM_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder.create(parent, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as ItemViewHolder).bind(item)
        }
    }

    @Suppress("PLUGIN_WARNING")
    fun onItemPressed(item: Item) {

        // if the device has two pane, we need to load the DetailFragment into the container
        // if the device has only one, we need to make an activity transition
        if (twoPane) {
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ItemDetailFragment.ARG_ITEM_ID, item)
                }
            }
            parentActivity.item_search_no_item_selected.visibility = View.GONE
            parentActivity.item_search_detail_container.visibility = View.VISIBLE

            parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_search_detail_container, fragment)
                    .commit()
        } else {
            val intent = Intent(parentActivity, ItemDetailActivity::class.java).apply {
                putExtra(ItemDetailFragment.ARG_ITEM_ID, item)
            }
            parentActivity.startActivity(intent)
        }
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                    oldItem == newItem
        }
    }
}