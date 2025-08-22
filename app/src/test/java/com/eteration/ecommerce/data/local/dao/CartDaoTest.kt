package com.eteration.ecommerce.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.eteration.ecommerce.data.local.database.AppDatabase
import com.eteration.ecommerce.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CartDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var cartDao: CartDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        cartDao = database.cartDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insertCartItem should insert item into database`() = runBlocking {
        // Given
        val cartItem = CartItemEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            quantity = 1,
            id = 1,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis()
        )

        // When
        cartDao.insertCartItem(cartItem)

        // Then
        val items = cartDao.getAllCartItems().first()
        assertEquals(1, items.size)
        assertEquals(cartItem, items[0])
    }

    @Test
    fun `getCartItemByProductId should return item when exists`() = runBlocking {
        // Given
        val cartItem = CartItemEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            quantity = 1,
            id = 1,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis()
        )
        cartDao.insertCartItem(cartItem)

        // When
        val result = cartDao.getCartItemByProductId("1")

        // Then
        assertEquals(cartItem, result)
    }

    @Test
    fun `getCartItemByProductId should return null when not exists`() = runBlocking {
        // When
        val result = cartDao.getCartItemByProductId("nonexistent")

        // Then
        assertNull(result)
    }

    @Test
    fun `getCartItemCount should return correct count`() = runBlocking {
        // Given
        val cartItem1 = CartItemEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            quantity = 1,
            id = 1,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis()
        )
        val cartItem2 = CartItemEntity(
            productId = "12",
            productName = "Samsung",
            price = 10000.0,
            quantity = 1,
            id = 2,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis()
        )
        cartDao.insertCartItem(cartItem1)
        cartDao.insertCartItem(cartItem2)

        // When
        val count = cartDao.getCartItemCount().first()

        // Then
        assertEquals(2, count)
    }

    @Test
    fun `getTotalPrice should return correct total`() = runBlocking {
        // Given
        val cartItem1 = CartItemEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            quantity = 1,
            id = 1,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis()
        )
        val cartItem2 = CartItemEntity(
            productId = "12",
            productName = "Samsung",
            price = 10000.0,
            quantity = 1,
            id = 2,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis()
        )
        cartDao.insertCartItem(cartItem1)
        cartDao.insertCartItem(cartItem2)

        // When
        val totalPrice = cartDao.getTotalPrice().first()

        assertEquals(25000.0, totalPrice!!, 0.01)
    }

    @Test
    fun `updateQuantity should update item quantity`() = runBlocking {
        // Given
        val cartItem = CartItemEntity(
            productId = "12",
            productName = "Samsung",
            price = 10000.0,
            quantity = 1,
            id = 1,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis()
        )
        cartDao.insertCartItem(cartItem)

        // When
        cartDao.updateQuantity("12", 5)

        // Then
        val updatedItem = cartDao.getCartItemByProductId("12")
        assertEquals(5, updatedItem?.quantity)
    }

    @Test
    fun `deleteCartItemByProductId should remove item`() = runBlocking {
        // Given
        val cartItem = CartItemEntity(
            productId = "12",
            productName = "Samsung",
            price = 10000.0,
            quantity = 1,
            id = 1,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis()
        )
        cartDao.insertCartItem(cartItem)

        // When
        cartDao.deleteCartItemByProductId("12")

        // Then
        val items = cartDao.getAllCartItems().first()
        assertEquals(0, items.size)
    }

    @Test
    fun `clearCart should remove all items`() = runBlocking {
        // Given
        val cartItem1 = CartItemEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            quantity = 1,
            id = 1,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis()
        )
        val cartItem2 = CartItemEntity(
            productId = "12",
            productName = "Samsung",
            price = 10000.0,
            quantity = 1,
            id = 2,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis()
        )
        cartDao.insertCartItem(cartItem1)
        cartDao.insertCartItem(cartItem2)

        // When
        cartDao.clearCart()

        // Then
        val items = cartDao.getAllCartItems().first()
        assertEquals(0, items.size)
    }
}