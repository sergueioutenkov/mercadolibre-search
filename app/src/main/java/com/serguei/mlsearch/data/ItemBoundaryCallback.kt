package com.serguei.mlsearch.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.serguei.mlsearch.api.MercadoLibreService
import com.serguei.mlsearch.api.searchItems
import com.serguei.mlsearch.db.MercadoLibreLocalCache
import com.serguei.mlsearch.model.Item

class ItemBoundaryCallback(
        private val query: String,
        private val service: MercadoLibreService,
        private val cache: MercadoLibreLocalCache
) : PagedList.BoundaryCallback<Item>() {

    // When the API request is successful, increment this number by 50 (hardcoded for now, number obtained from ML api)
    private var lastRequestedOffset = 0

    private val _networkErrors = MutableLiveData<String>()

    // LiveData for network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // prevent multiple requests
    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Item) {
        requestAndSaveData(query)
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        searchItems(service, query, lastRequestedOffset, { repos ->
            cache.insert(repos) {
                lastRequestedOffset += 50
                isRequestInProgress = false
            }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }
}