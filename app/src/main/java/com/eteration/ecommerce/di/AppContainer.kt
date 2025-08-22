package com.eteration.ecommerce.di

import android.content.Context
import com.eteration.ecommerce.data.local.database.AppDatabase
import com.eteration.ecommerce.data.remote.api.RetrofitBuilder
import com.eteration.ecommerce.data.repository.CartRepositoryImpl
import com.eteration.ecommerce.data.repository.FavoriteRepositoryImpl
import com.eteration.ecommerce.data.repository.ProductRepositoryImpl
import com.eteration.ecommerce.domain.repository.CartRepository
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import com.eteration.ecommerce.domain.repository.ProductRepository
import com.eteration.ecommerce.domain.usecase.AddToCartUseCase
import com.eteration.ecommerce.domain.usecase.GetProductsUseCase
import com.eteration.ecommerce.domain.usecase.ToggleFavoriteUseCase

/**
 * Simple dependency injection container
 * Provides singleton instances of repositories and use cases
 */
class AppContainer(private val context: Context) {

    // Database instance
    private val database by lazy { AppDatabase.getInstance(context) }

    // DAO instances
    private val cartDao by lazy { database.cartDao() }
    private val favoriteDao by lazy { database.favoriteDao() }

    // API instances
    private val productApi by lazy { RetrofitBuilder.productApi }

    // Repositories
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(productApi)
    }

    val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(cartDao)
    }

    val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepositoryImpl(favoriteDao)
    }

    // Use Cases
    val getProductsUseCase: GetProductsUseCase by lazy {
        GetProductsUseCase(productRepository)
    }

    val addToCartUseCase: AddToCartUseCase by lazy {
        AddToCartUseCase(cartRepository)
    }

    val toggleFavoriteUseCase: ToggleFavoriteUseCase by lazy {
        ToggleFavoriteUseCase(favoriteRepository)
    }

    companion object {
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