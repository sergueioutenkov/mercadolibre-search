package com.serguei.mlsearch

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.serguei.mlsearch.api.MercadoLibreService
import com.serguei.mlsearch.data.MercadoLibreItemsRepository
import com.serguei.mlsearch.db.ItemDatabase
import com.serguei.mlsearch.db.MercadoLibreLocalCache
import com.serguei.mlsearch.ui.ViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [MercadoLibreLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): MercadoLibreLocalCache {
        val database = ItemDatabase.getInstance(context)
        return MercadoLibreLocalCache(database.itemsDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [MercadoLibreItemsRepository] based on the [MercadoLibreService] and a
     * [MercadoLibreLocalCache]
     */
    private fun provideMercadoLibreRepository(context: Context): MercadoLibreItemsRepository {
        return MercadoLibreItemsRepository(MercadoLibreService.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideMercadoLibreRepository(context))
    }

}