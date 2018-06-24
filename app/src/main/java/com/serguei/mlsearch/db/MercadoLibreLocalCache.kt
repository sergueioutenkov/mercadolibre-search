package com.serguei.mlsearch.db

import android.arch.paging.DataSource
import android.util.Log
import com.serguei.mlsearch.model.Item
import java.util.concurrent.Executor

private const val TAG = "MercadoLibreLocalCache"

class MercadoLibreLocalCache(
        private val itemDao: ItemDao,
        private val ioExecutor: Executor
) {

    /**
     * Inserts the items to the DB, on a background thread
     */
    fun insert(items: List<Item>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d(TAG, "inserting ${items.size} items")
            itemDao.insert(items)
            insertFinished()
        }
    }

    /**
     * Clears cache
     */
    fun clear() {
        ioExecutor.execute {
            itemDao.clearCache()
        }
    }

    /**
     * Gets items by title
     */
    fun itemsByTitle(title: String): DataSource.Factory<Int, Item> {
        val query = "%${title.replace(' ', '%')}%"
        return itemDao.itemsByTitle(query)
    }
}