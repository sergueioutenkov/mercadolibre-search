package com.serguei.mlsearch.db

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.serguei.mlsearch.model.Item

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<Item>)

    // Look for items that contain the query string in the title
    @Query("SELECT * FROM items WHERE title LIKE :queryString ORDER BY ratingAverage DESC")
    fun itemsByTitle(queryString: String): DataSource.Factory<Int, Item>

    @Query("DELETE FROM items")
    fun clearCache()
}