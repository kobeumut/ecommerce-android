package com.eteration.ecommerce.data.mapper

import com.eteration.ecommerce.data.local.entity.CartItemEntity
import com.eteration.ecommerce.data.local.entity.FavoriteEntity
import com.eteration.ecommerce.data.remote.model.ProductResponse
import com.eteration.ecommerce.domain.model.CartItem
import com.eteration.ecommerce.domain.model.Product
import org.junit.Assert.*
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `ProductResponse toDomainModel should map correctly`() {
        // Given
        val productResponse = ProductResponse(
            id = "1",
            name = "iPhone 13",
            image = "https://example.com/iphone13.jpg",
            price = "15000",
            description = "Latest iPhone model",
            model = "13",
            brand = "Apple",
            createdAt = "2023-01-01"
        )

        // When
        val product = productResponse.toDomainModel()

        // Then
        assertEquals("1", product.id)
        assertEquals("iPhone 13", product.name)
        assertEquals("https://example.com/iphone13.jpg", product.image)
        assertEquals(15000.0, product.price, 0.01)
        assertEquals("Latest iPhone model", product.description)
        assertEquals("13", product.model)
        assertEquals("Apple", product.brand)
        assertEquals("2023-01-01", product.createdAt)
    }

    @Test
    fun `ProductResponse toDomainModel should handle invalid price`() {
        // Given
        val productResponse = ProductResponse(
            id = "1",
            name = "iPhone 13",
            image = "https://example.com/iphone13.jpg",
            price = "invalid",
            description = "Latest iPhone model",
            model = "13",
            brand = "Apple",
            createdAt = "2023-01-01"
        )

        // When
        val product = productResponse.toDomainModel()

        // Then
        assertEquals(0.0, product.price, 0.01)
    }

    @Test
    fun `CartItemEntity toDomainModel should map correctly`() {
        // Given
        val cartItemEntity = CartItemEntity(
            id = 1,
            productId = "1",
            productName = "iPhone 13",
            productImage = "https://example.com/iphone13.jpg",
            price = 15000.0,
            quantity = 2
        )

        // When
        val cartItem = cartItemEntity.toDomainModel()

        // Then
        assertEquals(1, cartItem.id)
        assertEquals("1", cartItem.productId)
        assertEquals("iPhone 13", cartItem.productName)
        assertEquals("https://example.com/iphone13.jpg", cartItem.productImage)
        assertEquals(15000.0, cartItem.price, 0.01)
        assertEquals(2, cartItem.quantity)
    }

    @Test
    fun `CartItem toEntity should map correctly`() {
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
        val cartItemEntity = cartItem.toEntity()

        // Then
        assertEquals(1, cartItemEntity.id)
        assertEquals("1", cartItemEntity.productId)
        assertEquals("iPhone 13", cartItemEntity.productName)
        assertEquals("https://example.com/iphone13.jpg", cartItemEntity.productImage)
        assertEquals(15000.0, cartItemEntity.price, 0.01)
        assertEquals(2, cartItemEntity.quantity)
    }

    @Test
    fun `Product toCartItemEntity should map correctly with default quantity`() {
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
        val cartItemEntity = product.toCartItemEntity()

        // Then
        assertEquals("1", cartItemEntity.productId)
        assertEquals("iPhone 13", cartItemEntity.productName)
        assertEquals("https://example.com/iphone13.jpg", cartItemEntity.productImage)
        assertEquals(15000.0, cartItemEntity.price, 0.01)
        assertEquals(1, cartItemEntity.quantity)
    }

    @Test
    fun `Product toCartItemEntity should map correctly with custom quantity`() {
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
        val cartItemEntity = product.toCartItemEntity(3)

        // Then
        assertEquals("1", cartItemEntity.productId)
        assertEquals("iPhone 13", cartItemEntity.productName)
        assertEquals("https://example.com/iphone13.jpg", cartItemEntity.productImage)
        assertEquals(15000.0, cartItemEntity.price, 0.01)
        assertEquals(3, cartItemEntity.quantity)
    }

    @Test
    fun `Product toFavoriteEntity should map correctly`() {
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
        val favoriteEntity = product.toFavoriteEntity()

        // Then
        assertEquals("1", favoriteEntity.productId)
        assertEquals("iPhone 13", favoriteEntity.productName)
        assertEquals("https://example.com/iphone13.jpg", favoriteEntity.productImage)
        assertEquals(15000.0, favoriteEntity.price, 0.01)
        assertEquals("Latest iPhone model", favoriteEntity.description)
        assertEquals("13", favoriteEntity.model)
        assertEquals("Apple", favoriteEntity.brand)
    }

    @Test
    fun `FavoriteEntity toDomainModel should map correctly`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = "1",
            productName = "iPhone 13",
            productImage = "https://example.com/iphone13.jpg",
            price = 15000.0,
            description = "Latest iPhone model",
            model = "13",
            brand = "Apple"
        )

        // When
        val product = favoriteEntity.toDomainModel()

        // Then
        assertEquals("1", product.id)
        assertEquals("iPhone 13", product.name)
        assertEquals("https://example.com/iphone13.jpg", product.image)
        assertEquals(15000.0, product.price, 0.01)
        assertEquals("Latest iPhone model", product.description)
        assertEquals("13", product.model)
        assertEquals("Apple", product.brand)
        assertEquals("", product.createdAt) // Should be empty as it's not in FavoriteEntity
    }
}