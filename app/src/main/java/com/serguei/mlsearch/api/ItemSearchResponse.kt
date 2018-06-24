package com.serguei.mlsearch.api

import com.google.gson.annotations.SerializedName
import com.serguei.mlsearch.model.Item

data class ItemSearchResponse(
        @SerializedName("results") val items: List<Item> = emptyList()
)