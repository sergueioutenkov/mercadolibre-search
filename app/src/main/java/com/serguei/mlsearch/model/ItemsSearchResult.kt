package com.serguei.mlsearch.model

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

/**
 * Data class representing a search result on ML search items API
 */
data class ItemsSearchResult(
        val data: LiveData<PagedList<Item>>,
        val networkErrors: LiveData<String>
)