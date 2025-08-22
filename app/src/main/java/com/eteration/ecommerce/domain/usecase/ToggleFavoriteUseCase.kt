package com.eteration.ecommerce.domain.usecase

import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.repository.FavoriteRepository

/**
 * Use case for toggling favorite status
 */
class ToggleFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(product: Product) {
        favoriteRepository.toggleFavorite(product)
    }
}