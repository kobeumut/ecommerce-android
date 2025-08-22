package com.eteration.ecommerce.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eteration.ecommerce.data.local.dao.CartDao
import com.eteration.ecommerce.data.local.dao.FavoriteDao
import com.eteration.ecommerce.data.local.entity.CartItemEntity
import com.eteration.ecommerce.data.local.entity.FavoriteEntity
import com.eteration.ecommerce.presentation.utils.AppConstants.ETERATION_DATABASE

@Database(
    entities = [CartItemEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    ETERATION_DATABASE
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}