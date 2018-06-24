package com.serguei.mlsearch.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.serguei.mlsearch.model.Item

@Database(
        entities = [Item::class],
        version = 3,
        exportSchema = false
)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemsDao(): ItemDao

    companion object {

        @Volatile
        private var INSTANCE: ItemDatabase? = null

        fun getInstance(context: Context): ItemDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        ItemDatabase::class.java, "MercadoLibre.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}