package com.eteration.ecommerce.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.eteration.ecommerce.data.local.database.AppDatabase
import com.eteration.ecommerce.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var favoriteDao: FavoriteDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        favoriteDao = database.favoriteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insertFavorite should insert favorite into database`() = runBlocking {
        // Given
        val favorite = FavoriteEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis(),
            description = "",
            model = "Iphone 16",
            brand = "Apple",
        )

        // When
        favoriteDao.insertFavorite(favorite)

        // Then
        val favorites = favoriteDao.getAllFavorites().first()
        assertEquals(1, favorites.size)
        assertEquals(favorite, favorites[0])
    }

    @Test
    fun `getAllFavorites should return all favorites`() = runBlocking {
        // Given
        val favorite1 = FavoriteEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis(),
            description = "",
            model = "Iphone 16",
            brand = "Apple",
        )
        val favorite2 = FavoriteEntity(
            productId = "2",
            productName = "Samsung",
            price = 15000.0,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis(),
            description = "",
            model = "S25",
            brand = "Samsung",
        )
        favoriteDao.insertFavorite(favorite1)
        favoriteDao.insertFavorite(favorite2)

        // When
        val favorites = favoriteDao.getAllFavorites().first()

        // Then
        assertEquals(2, favorites.size)
        assertTrue(favorites.contains(favorite1))
        assertTrue(favorites.contains(favorite2))
    }

    @Test
    fun `getFavoriteIds should return all favorite IDs`() = runBlocking {
        // Given
        val favorite1 = FavoriteEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis(),
            description = "",
            model = "Iphone 16",
            brand = "Apple",
        )
        val favorite2 = FavoriteEntity(
            productId = "2",
            productName = "Samsung",
            price = 15000.0,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis(),
            description = "",
            model = "S25",
            brand = "Samsung",
        )
        favoriteDao.insertFavorite(favorite1)
        favoriteDao.insertFavorite(favorite2)

        // When
        val favoriteIds = favoriteDao.getFavoriteIds().first()

        // Then
        assertEquals(2, favoriteIds.size)
        assertTrue(favoriteIds.contains("1"))
        assertTrue(favoriteIds.contains("2"))
    }

    @Test
    fun `isFavorite should return true for existing favorite`() = runBlocking {
        // Given
        val favorite = FavoriteEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis(),
            description = "",
            model = "Iphone 16",
            brand = "Apple",
        )
        favoriteDao.insertFavorite(favorite)

        // When
        val result = favoriteDao.isFavorite("1")

        // Then
        assertTrue(result)
    }

    @Test
    fun `isFavorite should return false for non-existing favorite`() = runBlocking {
        // When
        val result = favoriteDao.isFavorite("nonexistent")

        // Then
        assertFalse(result)
    }

    @Test
    fun `deleteFavoriteById should remove favorite`() = runBlocking {
        // Given
        val favorite = FavoriteEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis(),
            description = "",
            model = "Iphone 16",
            brand = "Apple",
        )
        favoriteDao.insertFavorite(favorite)

        // When
        favoriteDao.deleteFavoriteById("1")

        // Then
        val favorites = favoriteDao.getAllFavorites().first()
        assertEquals(0, favorites.size)
    }

    @Test
    fun `clearFavorites should remove all favorites`() = runBlocking {
        // Given
        val favorite1 = FavoriteEntity(
            productId = "1",
            productName = "Apple",
            price = 15000.0,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis(),
            description = "",
            model = "Iphone 16",
            brand = "Apple",
        )
        val favorite2 = FavoriteEntity(
            productId = "2",
            productName = "Samsung",
            price = 15000.0,
            productImage = "https://example.com/iphone13.jpg",
            timestamp = System.currentTimeMillis(),
            description = "",
            model = "S25",
            brand = "Samsung",
        )
        favoriteDao.insertFavorite(favorite1)
        favoriteDao.insertFavorite(favorite2)

        // When
        favoriteDao.clearFavorites()

        // Then
        val favorites = favoriteDao.getAllFavorites().first()
        assertEquals(0, favorites.size)
    }
}