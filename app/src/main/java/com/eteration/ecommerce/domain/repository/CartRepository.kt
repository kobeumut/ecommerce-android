package com.eteration.ecommerce.domain.repository

import com.eteration.ecommerce.domain.model.CartItem
import com.eteration.ecommerce.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for cart operations
 */
interface CartRepository {
    fun getAllCartItems(): Flow<List<CartItem>>
    fun getCartItemCount(): Flow<Int>
    fun getTotalPrice(): Flow<Double>
    suspend fun addToCart(product: Product)
    suspend fun updateQuantity(productId: String, quantity: Int)
    suspend fun removeFromCart(productId: String)
    suspend fun clearCart()
    suspend fun isInCart(productId: String): Boolean
}