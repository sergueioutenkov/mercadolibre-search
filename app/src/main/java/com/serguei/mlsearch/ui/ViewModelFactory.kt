package com.serguei.mlsearch.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.serguei.mlsearch.data.MercadoLibreItemsRepository
import com.serguei.mlsearch.ui.search.SearchItemsViewModel

/**
 * Factory to create View models passing constructor arguments
 */
class ViewModelFactory(private val itemsRepository: MercadoLibreItemsRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchItemsViewModel::class.java)) {
            return SearchItemsViewModel(itemsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}