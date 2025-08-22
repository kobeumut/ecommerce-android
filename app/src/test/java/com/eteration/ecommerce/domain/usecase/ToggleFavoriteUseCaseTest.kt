package com.eteration.ecommerce.domain.usecase

import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.*

class ToggleFavoriteUseCaseTest {

    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setUp() {
        favoriteRepository = mock()
        toggleFavoriteUseCase = ToggleFavoriteUseCase(favoriteRepository)
    }

    @Test
    fun `invoke should call repository toggleFavorite with correct product`() = runTest {
        // Given
        val product = TestUtils.sampleProducts[0]

        // When
        toggleFavoriteUseCase.invoke(product)

        // Then
        verify(favoriteRepository).toggleFavorite(product)
    }

}