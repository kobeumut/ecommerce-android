package com.eteration.ecommerce.domain.model

import org.junit.Assert.*
import org.junit.Test

class ProductTest {

    @Test
    fun `getFormattedPrice should format price correctly`() {
        // Given
        val product = Product(
            id = "1",
            name = "iPhone 13",
            image = "https://example.com/iphone13.jpg",
            price = 15000.0,
            description = "Latest iPhone model",
            model = "13",
            brand = "Apple",
            createdAt = "2023-01-01"
        )

        // When
        val formattedPrice = product.getFormattedPrice()

        // Then
        assertEquals("15000.00 ₺", formattedPrice)
    }

    @Test
    fun `getFormattedPrice should handle decimal values correctly`() {
        // Given
        val product = Product(
            id = "1",
            name = "iPhone 13",
            image = "https://example.com/iphone13.jpg",
            price = 15000.50,
            description = "Latest iPhone model",
            model = "13",
            brand = "Apple",
            createdAt = "2023-01-01"
        )

        // When
        val formattedPrice = product.getFormattedPrice()

        // Then
        assertEquals("15000.50 ₺", formattedPrice)
    }

    @Test
    fun `Product should be created with correct values`() {
        // Given
        val id = "1"
        val name = "iPhone 13"
        val image = "https://example.com/iphone13.jpg"
        val price = 15000.0
        val description = "Latest iPhone model"
        val model = "13"
        val brand = "Apple"
        val createdAt = "2023-01-01"

        // When
        val product = Product(id, name, image, price, description, model, brand, createdAt)

        // Then
        assertEquals(id, product.id)
        assertEquals(name, product.name)
        assertEquals(image, product.image)
        assertEquals(price, product.price, 0.01)
        assertEquals(description, product.description)
        assertEquals(model, product.model)
        assertEquals(brand, product.brand)
        assertEquals(createdAt, product.createdAt)
    }
}