package com.eteration.ecommerce.domain.usecase

import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.repository.CartRepository

/**
 * Use case for adding products to cart
 */
class AddToCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(product: Product) {
        cartRepository.addToCart(product)
    }
}