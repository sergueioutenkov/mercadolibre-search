package com.serguei.mlsearch.ui.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.serguei.mlsearch.Injection
import com.serguei.mlsearch.R
import com.serguei.mlsearch.model.Item
import kotlinx.android.synthetic.main.item_list.*

class SearchItemsActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchItemsViewModel

    private var twoPane: Boolean = false
    private var adapter: ItemsAdapter? = null
    private lateinit var query: String

    override fun onResume() {
        super.onResume()

        // Prevent stupid search view from gaining focus everytime by setting focus to the list
        item_search_list.requestFocus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
                .get(SearchItemsViewModel::class.java)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        item_search_list.addItemDecoration(decoration)

        if (item_search_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        adapter = ItemsAdapter(twoPane, this)
        initAdapter()
        query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: ""
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: ""
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val mSearch = menu.findItem(R.id.action_search)

        val mSearchView = mSearch.actionView as SearchView
        mSearchView.queryHint = resources.getString(R.string.search_search_hint)
        mSearchView.maxWidth = Integer.MAX_VALUE

        if (query != "") {
            mSearchView.setQuery(query, true)
        }

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                query.trim().let {
                    hideKeyboard()
                    item_search_list.scrollToPosition(0)
                    viewModel.searchItem(it)
                    adapter?.submitList(null)
                    @Suppress("PLUGIN_WARNING")
                    if (twoPane) {
                        item_search_detail_container.visibility = View.GONE
                        item_search_no_item_selected.visibility = View.VISIBLE
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
        super.onSaveInstanceState(outState)
    }

    private fun initAdapter() {
        item_search_list.adapter = adapter
        viewModel.items.observe(this, Observer<PagedList<Item>> {
            showEmptyList(it?.size == 0)
            adapter?.submitList(it)
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            item_search_no_results.visibility = View.VISIBLE
            item_search_list.visibility = View.GONE
        } else {
            item_search_no_results.visibility = View.GONE
            item_search_list.visibility = View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}