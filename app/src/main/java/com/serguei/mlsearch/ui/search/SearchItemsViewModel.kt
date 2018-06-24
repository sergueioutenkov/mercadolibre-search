package com.serguei.mlsearch.ui.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.serguei.mlsearch.data.MercadoLibreItemsRepository
import com.serguei.mlsearch.model.Item
import com.serguei.mlsearch.model.ItemsSearchResult

class SearchItemsViewModel(private val itemsRepository: MercadoLibreItemsRepository) : ViewModel() {
    private val queryLiveData = MutableLiveData<String>()

    private val itemResult: LiveData<ItemsSearchResult> = Transformations.map(queryLiveData) {
        itemsRepository.search(it)
    }

    val items: LiveData<PagedList<Item>> = Transformations.switchMap(itemResult) { it ->
        return@switchMap it.data
    }

    val networkErrors: LiveData<String> = Transformations.switchMap(itemResult) { it ->
        return@switchMap it.networkErrors
    }

    fun searchItem(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun lastQueryValue(): String? = queryLiveData.value
}