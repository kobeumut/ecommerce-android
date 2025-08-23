package com.eteration.ecommerce.presentation.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eteration.ecommerce.MainDispatcherRule
import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import com.eteration.ecommerce.domain.usecase.AddToCartUseCase
import com.eteration.ecommerce.domain.usecase.ToggleFavoriteUseCase
import com.eteration.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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
    fun `checkFavoriteStatus should update isFavorite when product is favorite`() = runTest {
        // Given
        val productId = "1"
        whenever(favoriteRepository.isFavorite(productId)).thenReturn(true)

        // When
        viewModel.checkFavoriteStatus(productId)
        advanceUntilIdle()

        // Then
        verify(favoriteRepository).isFavorite(productId)
        assertTrue(viewModel.isFavorite.getOrAwaitValue())
    }

    @Test
    fun `checkFavoriteStatus should update isFavorite when product is not favorite`() = runTest {
        // Given
        val productId = "2"
        whenever(favoriteRepository.isFavorite(productId)).thenReturn(false)

        // When
        viewModel.checkFavoriteStatus(productId)
        advanceUntilIdle()

        // Then
        verify(favoriteRepository).isFavorite(productId)
        assertFalse(viewModel.isFavorite.getOrAwaitValue())
    }

    @Test
    fun `addToCart should call use case and post cartAddedEvent`() = runTest {
        // Given
        val product = TestUtils.sampleProducts[0]
        whenever(addToCartUseCase.invoke(any())).thenReturn(Unit)

        // When
        viewModel.addToCart(product)
        advanceUntilIdle()

        // Then
        verify(addToCartUseCase).invoke(product)

        val cartEvent = viewModel.cartAddedEvent.getOrAwaitValue()
        assertNotNull(cartEvent)
        assertTrue(cartEvent.contains(product.name))
    }

    @Test
    fun `toggleFavorite should call use case and toggle isFavorite state from false to true`() = runTest {
        // Given
        val product = TestUtils.sampleProducts[0]
        whenever(toggleFavoriteUseCase.invoke(any())).thenReturn(Unit)

        // Set initial state to false
        whenever(favoriteRepository.isFavorite(product.id)).thenReturn(false)
        viewModel.checkFavoriteStatus(product.id)
        advanceUntilIdle()

        // When
        viewModel.toggleFavorite(product)
        advanceUntilIdle()

        // Then
        verify(toggleFavoriteUseCase).invoke(product)
        assertTrue(viewModel.isFavorite.getOrAwaitValue())
    }

    @Test
    fun `toggleFavorite should call use case and toggle isFavorite state from true to false`() = runTest {
        // Given
        val product = TestUtils.sampleProducts[0]
        whenever(toggleFavoriteUseCase.invoke(any())).thenReturn(Unit)

        // Set initial state to true
        whenever(favoriteRepository.isFavorite(product.id)).thenReturn(true)
        viewModel.checkFavoriteStatus(product.id)
        advanceUntilIdle()

        // When
        viewModel.toggleFavorite(product)
        advanceUntilIdle()

        // Then
        verify(toggleFavoriteUseCase).invoke(product)
        assertFalse(viewModel.isFavorite.getOrAwaitValue())
    }

    @Test
    fun `setNameForLength should truncate long text with ellipsis`() {
        // Given
        val longText = "This is a very long product name that exceeds the maximum length"
        val maxLength = 20

        // When
        val result = viewModel.setNameForLength(longText, maxLength)

        // Then
        assertEquals("This is a very long ...", result)
        assertTrue(result.length <= maxLength + 3) // +3 for "..."
    }

    @Test
    fun `setNameForLength should return original text when within length limit`() {
        // Given
        val shortText = "Short name"
        val maxLength = 20

        // When
        val result = viewModel.setNameForLength(shortText, maxLength)

        // Then
        assertEquals(shortText, result)
    }

    @Test
    fun `setNameForLength should handle text exactly at max length`() {
        // Given
        val exactLengthText = "12345678901234567890" // exactly 20 chars
        val maxLength = 20

        // When
        val result = viewModel.setNameForLength(exactLengthText, maxLength)

        // Then
        assertEquals(exactLengthText, result)
    }

    @Test
    fun `setNameForLength should use default max length of 20 when not specified`() {
        // Given
        val longText = "This is a very long product name that exceeds twenty characters"

        // When
        val result = viewModel.setNameForLength(longText)

        // Then
        assertEquals("This is a very long ...", result)
    }

    @Test
    fun `setNameForLength should handle empty string`() {
        // Given
        val emptyText = ""
        val maxLength = 20

        // When
        val result = viewModel.setNameForLength(emptyText, maxLength)

        // Then
        assertEquals("", result)
    }

    @Test
    fun `setNameForLength should handle single character`() {
        // Given
        val singleChar = "A"
        val maxLength = 20

        // When
        val result = viewModel.setNameForLength(singleChar, maxLength)

        // Then
        assertEquals("A", result)
    }
}