package com.eteration.ecommerce.presentation.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.domain.usecase.AddToCartUseCase
import com.eteration.ecommerce.domain.usecase.ToggleFavoriteUseCase
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import com.eteration.ecommerce.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class ProductDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setUp() {
        addToCartUseCase = mock()
        toggleFavoriteUseCase = mock()
        favoriteRepository = mock()
        
        viewModel = ProductDetailViewModel(
            addToCartUseCase,
            toggleFavoriteUseCase,
            favoriteRepository
        )
    }

    @Test
    fun `setNameForLength should not truncate short text`() {
        // Given
        val shortText = "Short name"
        val maxLength = 20

        // When
        val result = viewModel.setNameForLength(shortText, maxLength)

        // Then
        assertEquals(shortText, result)
    }
}