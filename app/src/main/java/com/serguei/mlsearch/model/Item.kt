package com.serguei.mlsearch.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.itishka.gsonflatten.Flatten
import java.io.Serializable

/**
 * Data class that represents an item being offered in MercadoLibre
 */
@Entity(tableName = "items")
data class Item(
        @PrimaryKey @field:SerializedName("id") val id: String,
        @field:SerializedName("title") val title: String,
        @field:SerializedName("price") val price: Double,
        @field:SerializedName("thumbnail") val thumbnail: String?,
        @field:Flatten("shipping::free_shipping") val freeShipping: Boolean,
        @field:Flatten("reviews::rating_average") val ratingAverage: Double,
        @field:Flatten("reviews::total") val totalReviews: Int,
        @field:SerializedName("available_quantity") val availableQuantity: Int
) : Serializable

data class Description(
        @SerializedName("plain_text") val text: String
)