package com.eteration.ecommerce.data.repository

import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.data.local.dao.CartDao
import com.eteration.ecommerce.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class CartRepositoryImplTest {

    private lateinit var cartRepository: CartRepositoryImpl
    private lateinit var cartDao: CartDao

    @Before
    fun setUp() {
        cartDao = mock()
        cartRepository = CartRepositoryImpl(cartDao)
    }

    @Test
    fun `getAllCartItems should return mapped cart items from DAO`() = runTest {
        // Given
        val cartItemEntities = listOf(
            CartItemEntity(
                productId = "1",
                productName = "Apple",
                price = 15000.0,
                quantity = 2,
                id = 1,
                productImage = "https://example.com/iphone13.jpg",
                timestamp = System.currentTimeMillis()
            )
        )
        whenever(cartDao.getAllCartItems()).thenReturn(flowOf(cartItemEntities))

        // When
        val cartItems = cartRepository.getAllCartItems().first()

        // Then
        assertEquals(1, cartItems.size)
        assertEquals("1", cartItems[0].productId)
        assertEquals(2, cartItems[0].quantity)
        verify(cartDao).getAllCartItems()
    }

    @Test
    fun `getCartItemCount should return count from DAO`() = runTest {
        // Given
        val count = 5
        whenever(cartDao.getCartItemCount()).thenReturn(flowOf(count))

        // When
        val itemCount = cartRepository.getCartItemCount().first()

        // Then
        assertEquals(count, itemCount)
        verify(cartDao).getCartItemCount()
    }

    @Test
    fun `getTotalPrice should return total price from DAO`() = runTest {
        // Given
        val totalPrice = 42000.0
        whenever(cartDao.getTotalPrice()).thenReturn(flowOf(totalPrice))

        // When
        val price = cartRepository.getTotalPrice().first()

        // Then
        assertEquals(totalPrice, price, 0.01)
        verify(cartDao).getTotalPrice()
    }

    @Test
    fun `addToCart should increment quantity when item exists`() = runTest {
        // Given
        val product = TestUtils.sampleProducts[0]
        val existingItem = CartItemEntity(
            productId = product.id,
            price = product.price,
            quantity = 2,
            productName = product.name,
            productImage = product.image
        )
        whenever(cartDao.getCartItemByProductId(product.id)).thenReturn(existingItem)

        // When
        cartRepository.addToCart(product)

        // Then
        verify(cartDao).getCartItemByProductId(product.id)
        verify(cartDao).updateQuantity(product.id, 3)
        verify(cartDao, never()).insertCartItem(any())
    }

    @Test
    fun `addToCart should insert new item when item does not exist`() = runTest {
        // Given
        val product = TestUtils.sampleProducts[0]
        whenever(cartDao.getCartItemByProductId(product.id)).thenReturn(null)

        // When
        cartRepository.addToCart(product)

        // Then
        verify(cartDao).getCartItemByProductId(product.id)
        verify(cartDao).insertCartItem(any())
        verify(cartDao, never()).updateQuantity(any(), any())
    }

    @Test
    fun `updateQuantity should delete item when quantity is zero or negative`() = runTest {
        // Given
        val productId = "1"
        val quantity = 0

        // When
        cartRepository.updateQuantity(productId, quantity)

        // Then
        verify(cartDao).deleteCartItemByProductId(productId)
        verify(cartDao, never()).updateQuantity(any(), any())
    }

    @Test
    fun `updateQuantity should update quantity when quantity is positive`() = runTest {
        // Given
        val productId = "1"
        val quantity = 5

        // When
        cartRepository.updateQuantity(productId, quantity)

        // Then
        verify(cartDao).updateQuantity(productId, quantity)
        verify(cartDao, never()).deleteCartItemByProductId(any())
    }

    @Test
    fun `clearCart should call DAO to clear all items`() = runTest {
        // When
        cartRepository.clearCart()

        // Then
        verify(cartDao).clearCart()
    }

    @Test
    fun `isInCart should return false when item does not exist`() = runTest {
        // Given
        val productId = "1"
        whenever(cartDao.getCartItemByProductId(productId)).thenReturn(null)

        // When
        val result = cartRepository.isInCart(productId)

        // Then
        assertFalse(result)
        verify(cartDao).getCartItemByProductId(productId)
    }
}