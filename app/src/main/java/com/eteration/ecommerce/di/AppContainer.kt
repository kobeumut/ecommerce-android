package com.eteration.ecommerce.di

import android.annotation.SuppressLint
import android.content.Context
import com.eteration.ecommerce.data.local.database.AppDatabase
import com.eteration.ecommerce.data.remote.api.RetrofitBuilder
import com.eteration.ecommerce.data.repository.CartRepositoryImpl
import com.eteration.ecommerce.data.repository.ProductRepositoryImpl
import com.eteration.ecommerce.domain.repository.CartRepository
import com.eteration.ecommerce.domain.repository.ProductRepository
import com.eteration.ecommerce.domain.usecase.GetProductsUseCase

/**
 * Simple dependency injection container
 * Provides singleton instances of repositories and use cases
 */
class AppContainer(private val context: Context) {

    // Database instance
    private val database by lazy { AppDatabase.getInstance(context) }

    // DAO instances
    private val cartDao by lazy { database.cartDao() }
    // API instances
    private val productApi by lazy { RetrofitBuilder.productApi }
    val getProductsUseCase: GetProductsUseCase by lazy {
        GetProductsUseCase(productRepository)
    }
    // Repositories
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(productApi)
    }

    val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(cartDao)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: AppContainer? = null

        fun getInstance(context: Context): AppContainer {
            return INSTANCE ?: synchronized(this) {
                val instance = AppContainer(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}