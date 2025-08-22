package com.eteration.ecommerce.data.repository

import com.eteration.ecommerce.data.local.dao.CartDao
import com.eteration.ecommerce.data.mapper.toCartItemEntity
import com.eteration.ecommerce.data.mapper.toDomainModel
import com.eteration.ecommerce.domain.model.CartItem
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of CartRepository
 */
class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {

    override fun getAllCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getCartItemCount(): Flow<Int> {
        return cartDao.getCartItemCount()
    }

    override fun getTotalPrice(): Flow<Double> {
        return cartDao.getTotalPrice().map { it ?: 0.0 }
    }

    override suspend fun addToCart(product: Product) {
        val existingItem = cartDao.getCartItemByProductId(product.id)
        if (existingItem != null) {
            // If item exists, increment quantity
            cartDao.updateQuantity(product.id, existingItem.quantity + 1)
        } else {
            // Add new item
            cartDao.insertCartItem(product.toCartItemEntity())
        }
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteCartItemByProductId(productId)
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }

    override suspend fun removeFromCart(productId: String) {
        cartDao.deleteCartItemByProductId(productId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }

    override suspend fun isInCart(productId: String): Boolean {
        return cartDao.getCartItemByProductId(productId) != null
    }
}