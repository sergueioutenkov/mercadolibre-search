package com.serguei.mlsearch.data

import android.arch.paging.LivePagedListBuilder
import android.util.Log
import com.serguei.mlsearch.api.MercadoLibreService
import com.serguei.mlsearch.db.MercadoLibreLocalCache
import com.serguei.mlsearch.model.ItemsSearchResult

private const val TAG = "MercadoLibreItemsRepo"

class MercadoLibreItemsRepository(
        private val service: MercadoLibreService,
        private val cache: MercadoLibreLocalCache
) {
    fun search(query: String): ItemsSearchResult {
        Log.d(TAG, "New query: $query")

        //Clear previous search, the API might bring us new items
        cache.clear()

        // Get data source factory from the local cache
        val dataSourceFactory = cache.itemsByTitle(query)

        // Construct the boundary callback
        val boundaryCallback = ItemBoundaryCallback(query, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        return ItemsSearchResult(data, networkErrors)
    }


    companion object {
        // Retrieve 20 items at a time from the DB
        private const val DATABASE_PAGE_SIZE = 20
    }
}