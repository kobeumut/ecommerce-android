package com.eteration.ecommerce.data.repository

import com.eteration.ecommerce.data.local.dao.FavoriteDao
import com.eteration.ecommerce.data.mapper.toDomainModel
import com.eteration.ecommerce.data.mapper.toFavoriteEntity
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of FavoriteRepository
 */
class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<Product>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getFavoriteIds(): Flow<List<String>> {
        return favoriteDao.getFavoriteIds()
    }

    override suspend fun toggleFavorite(product: Product) {
        if (favoriteDao.isFavorite(product.id)) {
            favoriteDao.deleteFavoriteById(product.id)
        } else {
            favoriteDao.insertFavorite(product.toFavoriteEntity())
        }
    }

    override suspend fun isFavorite(productId: String): Boolean {
        return favoriteDao.isFavorite(productId)
    }

    override suspend fun clearFavorites() {
        favoriteDao.clearFavorites()
    }
}