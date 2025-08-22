package com.eteration.ecommerce.domain.model

import org.junit.Assert.*
import org.junit.Test

class CartItemTest {

    @Test
    fun `getTotalPrice should calculate correctly`() {
        // Given
        val cartItem = CartItem(
            id = 1,
            productId = "1",
            productName = "iPhone 13",
            productImage = "https://example.com/iphone13.jpg",
            price = 15000.0,
            quantity = 2
        )

        // When
        val totalPrice = cartItem.getTotalPrice()

        // Then
        assertEquals(30000.0, totalPrice, 0.01)
    }

    @Test
    fun `getFormattedTotalPrice should format total price correctly`() {
        // Given
        val cartItem = CartItem(
            id = 1,
            productId = "1",
            productName = "iPhone 13",
            productImage = "https://example.com/iphone13.jpg",
            price = 15000.0,
            quantity = 2
        )

        // When
        val formattedTotalPrice = cartItem.getFormattedTotalPrice()

        // Then
        assertEquals("30000.00 ₺", formattedTotalPrice)
    }

    @Test
    fun `getFormattedPrice should format unit price correctly`() {
        // Given
        val cartItem = CartItem(
            id = 1,
            productId = "1",
            productName = "iPhone 13",
            productImage = "https://example.com/iphone13.jpg",
            price = 15000.0,
            quantity = 2
        )

        // When
        val formattedPrice = cartItem.getFormattedPrice()

        // Then
        assertEquals("15000.00 ₺", formattedPrice)
    }

    @Test
    fun `CartItem should be created with correct default values`() {
        // Given
        val productId = "1"
        val productName = "iPhone 13"
        val productImage = "https://example.com/iphone13.jpg"
        val price = 15000.0

        // When
        val cartItem = CartItem(
            productId = productId,
            productName = productName,
            productImage = productImage,
            price = price
        )

        // Then
        assertEquals(0, cartItem.id)
        assertEquals(productId, cartItem.productId)
        assertEquals(productName, cartItem.productName)
        assertEquals(productImage, cartItem.productImage)
        assertEquals(price, cartItem.price, 0.01)
        assertEquals(1, cartItem.quantity)
    }

    @Test
    fun `CartItem should be created with custom values`() {
        // Given
        val id: Long = 1
        val productId = "1"
        val productName = "iPhone 13"
        val productImage = "https://example.com/iphone13.jpg"
        val price = 15000.0
        val quantity = 3

        // When
        val cartItem = CartItem(id, productId, productName, productImage, price, quantity)

        // Then
        assertEquals(id, cartItem.id)
        assertEquals(productId, cartItem.productId)
        assertEquals(productName, cartItem.productName)
        assertEquals(productImage, cartItem.productImage)
        assertEquals(price, cartItem.price, 0.01)
        assertEquals(quantity, cartItem.quantity)
    }
}