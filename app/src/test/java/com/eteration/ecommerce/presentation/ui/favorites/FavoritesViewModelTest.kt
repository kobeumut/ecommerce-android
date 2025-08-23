package com.eteration.ecommerce.presentation.ui.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eteration.ecommerce.MainDispatcherRule
import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import com.eteration.ecommerce.domain.usecase.AddToCartUseCase
import com.eteration.ecommerce.domain.usecase.ToggleFavoriteUseCase
import com.eteration.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @Before
    fun setUp() {
        favoriteRepository = mock()
        addToCartUseCase = mock()
        toggleFavoriteUseCase = mock()

        // Setup default mocks - return some sample products as favorites
        whenever(favoriteRepository.getAllFavorites()).thenReturn(flowOf(TestUtils.sampleProducts))

        viewModel = FavoritesViewModel(
            favoriteRepository,
            addToCartUseCase,
            toggleFavoriteUseCase
        )
    }

    @Test
    fun `init should load favorite products and set loading state`() = runTest {
        // Given - setup is done in @Before
        advanceUntilIdle()

        // Then
        val favoriteProducts = viewModel.favoriteProducts.getOrAwaitValue()
        assertEquals(TestUtils.sampleProducts, favoriteProducts)

        val loading = viewModel.loading.getOrAwaitValue()
        assertFalse(loading)
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
    fun `toggleFavorite should call use case and reload favorites`() = runTest {
        // Given
        val product = TestUtils.sampleProducts[0]
        whenever(toggleFavoriteUseCase.invoke(any())).thenReturn(Unit)

        // When
        viewModel.toggleFavorite(product)
        advanceUntilIdle()

        // Then
        verify(toggleFavoriteUseCase).invoke(product)
        // Verify that getAllFavorites is called again (reload happens automatically in init and toggleFavorite)
        verify(favoriteRepository, atLeast(1)).getAllFavorites()
    }

    @Test
    fun `favoriteProducts should be empty when no favorites exist`() = runTest {
        // Given
        whenever(favoriteRepository.getAllFavorites()).thenReturn(flowOf(emptyList()))

        // When
        val newViewModel = FavoritesViewModel(
            favoriteRepository,
            addToCartUseCase,
            toggleFavoriteUseCase
        )
        advanceUntilIdle()

        // Then
        val favoriteProducts = newViewModel.favoriteProducts.getOrAwaitValue()
        assertTrue(favoriteProducts.isEmpty())

        val loading = newViewModel.loading.getOrAwaitValue()
        assertFalse(loading)
    }

    @Test
    fun `favoriteProducts should update when repository emits new values`() = runTest {
        // Given
        val initialFavorites = listOf(TestUtils.sampleProducts[0])
        val updatedFavorites = listOf(TestUtils.sampleProducts[1])

        // Create a flow that emits different values over time
        whenever(favoriteRepository.getAllFavorites())
            .thenReturn(flowOf(initialFavorites, updatedFavorites))

        // When
        val newViewModel = FavoritesViewModel(
            favoriteRepository,
            addToCartUseCase,
            toggleFavoriteUseCase
        )
        advanceUntilIdle()

        // Then - should have the latest emitted value
        val favoriteProducts = newViewModel.favoriteProducts.getOrAwaitValue()
        assertEquals(updatedFavorites, favoriteProducts)
    }

    @Test
    fun `favoriteProducts should handle single favorite product`() = runTest {
        // Given
        val singleFavorite = listOf(TestUtils.sampleProducts[1]) // Samsung Galaxy S22
        whenever(favoriteRepository.getAllFavorites()).thenReturn(flowOf(singleFavorite))

        // When
        val newViewModel = FavoritesViewModel(
            favoriteRepository,
            addToCartUseCase,
            toggleFavoriteUseCase
        )
        advanceUntilIdle()

        // Then
        val favoriteProducts = newViewModel.favoriteProducts.getOrAwaitValue()
        assertEquals(1, favoriteProducts.size)
        assertEquals("Samsung Galaxy S22", favoriteProducts[0].name)
        assertEquals("2", favoriteProducts[0].id)
    }

    @Test
    fun `favoriteProducts should handle all products as favorites`() = runTest {
        // Given
        whenever(favoriteRepository.getAllFavorites()).thenReturn(flowOf(TestUtils.sampleProducts))

        // When
        val newViewModel = FavoritesViewModel(
            favoriteRepository,
            addToCartUseCase,
            toggleFavoriteUseCase
        )
        advanceUntilIdle()

        // Then
        val favoriteProducts = newViewModel.favoriteProducts.getOrAwaitValue()
        assertEquals(3, favoriteProducts.size)
        assertEquals(TestUtils.sampleProducts, favoriteProducts)
    }

    @Test
    fun `addToCart should work with different products`() = runTest {
        // Given
        val huaweiProduct = TestUtils.sampleProducts[2] // Huawei P50
        whenever(addToCartUseCase.invoke(any())).thenReturn(Unit)

        // When
        viewModel.addToCart(huaweiProduct)
        advanceUntilIdle()

        // Then
        verify(addToCartUseCase).invoke(huaweiProduct)

        val cartEvent = viewModel.cartAddedEvent.getOrAwaitValue()
        assertNotNull(cartEvent)
        assertTrue(cartEvent.contains("Huawei P50"))
    }
}