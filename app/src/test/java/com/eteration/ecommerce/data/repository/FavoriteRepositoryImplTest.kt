package com.eteration.ecommerce.data.repository

import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.data.local.dao.FavoriteDao
import com.eteration.ecommerce.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class FavoriteRepositoryImplTest {

    private lateinit var favoriteRepository: FavoriteRepositoryImpl
    private lateinit var favoriteDao: FavoriteDao

    @Before
    fun setUp() {
        favoriteDao = mock()
        favoriteRepository = FavoriteRepositoryImpl(favoriteDao)
    }

    @Test
    fun `getAllFavorites should return mapped favorite products from DAO`() = runTest {
        // Given
        val favoriteEntities = listOf(
            FavoriteEntity(
                productId = "1",
                productName = "Apple",
                price = 15000.0,
                productImage = "https://example.com/iphone13.jpg",
                timestamp = System.currentTimeMillis(),
                description = "",
                model = "Iphone 16",
                brand = "Apple",
            )
        )
        whenever(favoriteDao.getAllFavorites()).thenReturn(flowOf(favoriteEntities))

        // When
        val favorites = favoriteRepository.getAllFavorites().first()

        // Then
        assertEquals(1, favorites.size)
        assertEquals("1", favorites[0].id)
        verify(favoriteDao).getAllFavorites()
    }

    @Test
    fun `getFavoriteIds should return favorite IDs from DAO`() = runTest {
        // Given
        val favoriteIds = listOf("1", "2", "3")
        whenever(favoriteDao.getFavoriteIds()).thenReturn(flowOf(favoriteIds))

        // When
        val ids = favoriteRepository.getFavoriteIds().first()

        // Then
        assertEquals(favoriteIds, ids)
        verify(favoriteDao).getFavoriteIds()
    }

    @Test
    fun `toggleFavorite should delete favorite when it exists`() = runTest {
        // Given
        val product = TestUtils.sampleProducts[0]
        whenever(favoriteDao.isFavorite(product.id)).thenReturn(true)

        // When
        favoriteRepository.toggleFavorite(product)

        // Then
        verify(favoriteDao).isFavorite(product.id)
        verify(favoriteDao).deleteFavoriteById(product.id)
        verify(favoriteDao, never()).insertFavorite(any())
    }

    @Test
    fun `isFavorite should return true when product is favorite`() = runTest {
        // Given
        val productId = "1"
        whenever(favoriteDao.isFavorite(productId)).thenReturn(true)

        // When
        val result = favoriteRepository.isFavorite(productId)

        // Then
        assertTrue(result)
        verify(favoriteDao).isFavorite(productId)
    }

    @Test
    fun `clearFavorites should call DAO to clear all favorites`() = runTest {
        // When
        favoriteRepository.clearFavorites()

        // Then
        verify(favoriteDao).clearFavorites()
    }
}