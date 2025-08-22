package com.eteration.ecommerce.domain.repository

import com.eteration.ecommerce.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for favorite operations
 */
interface FavoriteRepository {
    fun getAllFavorites(): Flow<List<Product>>
    fun getFavoriteIds(): Flow<List<String>>
    suspend fun toggleFavorite(product: Product)
    suspend fun isFavorite(productId: String): Boolean
    suspend fun clearFavorites()
}