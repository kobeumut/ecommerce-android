package com.eteration.ecommerce.presentation.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eteration.ecommerce.MainDispatcherRule
import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.domain.model.Filter
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.usecase.AddToCartUseCase
import com.eteration.ecommerce.domain.usecase.GetProductsUseCase
import com.eteration.ecommerce.domain.usecase.ToggleFavoriteUseCase
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import com.eteration.ecommerce.getOrAwaitValue
import com.eteration.ecommerce.presentation.utils.ViewState
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
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setUp() {
        getProductsUseCase = mock()
        addToCartUseCase = mock()
        toggleFavoriteUseCase = mock()
        favoriteRepository = mock()

        // Mock favorite repository to return empty list by default
        whenever(favoriteRepository.getFavoriteIds()).thenReturn(flowOf(emptyList()))

        // Mock initial products call
        whenever(getProductsUseCase.invoke(any(), any())).thenReturn(flowOf(Result.success(TestUtils.sampleProducts)))

        viewModel = HomeViewModel(
            getProductsUseCase,
            addToCartUseCase,
            toggleFavoriteUseCase,
            favoriteRepository
        )
    }

    @Test
    fun `loadProducts should update productsState with success when use case succeeds`() = runTest {
        // Given
        val products = TestUtils.sampleProducts
        whenever(getProductsUseCase.invoke(any(), any())).thenReturn(flowOf(Result.success(products)))

        // When
        viewModel.loadProducts()
        advanceUntilIdle()

        // Then
        val value = viewModel.productsState.getOrAwaitValue()
        assertTrue(value is ViewState.Success)
        assertEquals(products, (value as ViewState.Success).data)
    }

    @Test
    fun `loadProducts should update productsState with error when use case fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        whenever(getProductsUseCase.invoke(any(), any())).thenReturn(flowOf(Result.failure(exception)))

        // When
        viewModel.loadProducts()
        advanceUntilIdle()

        // Then
        val value = viewModel.productsState.getOrAwaitValue()
        assertTrue(value is ViewState.Error)
        assertEquals("Network error", (value as ViewState.Error).message)
    }

    @Test
    fun `searchProducts should update searchQuery and trigger loadProducts`() = runTest {
        // Given
        val query = "iPhone"
        whenever(getProductsUseCase.invoke(any(), any())).thenReturn(flowOf(Result.success(emptyList())))

        // When
        viewModel.searchProducts(query)
        advanceUntilIdle()

        // Then
        assertEquals(query, viewModel.searchQuery.value)
        // Verify that loadProducts was called with the correct query
        verify(getProductsUseCase, atLeast(1)).invoke(eq(query), any())
    }

    @Test
    fun `applyFilter should update currentFilter and trigger loadProducts`() = runTest {
        // Given
        val filter = Filter(selectedBrands = setOf("Apple"))
        whenever(getProductsUseCase.invoke(any(), any())).thenReturn(flowOf(Result.success(emptyList())))

        // When
        viewModel.applyFilter(filter)
        advanceUntilIdle()

        // Then
        assertEquals(filter, viewModel.currentFilter.value)
        // Verify that loadProducts was called with the correct filter
        verify(getProductsUseCase, atLeast(1)).invoke(any(), eq(filter))
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
        // Check that cartAddedEvent was posted
        val cartEvent = viewModel.cartAddedEvent.getOrAwaitValue()
        assertNotNull(cartEvent)
        assertTrue(cartEvent.contains(product.model))
    }

    @Test
    fun `toggleFavorite should call use case`() = runTest {
        // Given
        val product = TestUtils.sampleProducts[0]
        whenever(toggleFavoriteUseCase.invoke(any())).thenReturn(Unit)

        // When
        viewModel.toggleFavorite(product)
        advanceUntilIdle()

        // Then
        verify(toggleFavoriteUseCase).invoke(product)
    }

    @Test
    fun `retry should call loadProducts`() = runTest {
        // Given
        whenever(getProductsUseCase.invoke(any(), any())).thenReturn(flowOf(Result.success(emptyList())))

        // When
        viewModel.retry()
        advanceUntilIdle()

        // Then
        // Since loadProducts is called during init and retry, verify at least 2 calls
        verify(getProductsUseCase, atLeast(2)).invoke(any(), any())
    }

    @Test
    fun `favoriteIds should be updated when repository emits new values`() = runTest {
        // Given
        val favoriteIds = listOf("1", "2")
        whenever(favoriteRepository.getFavoriteIds()).thenReturn(flowOf(favoriteIds))

        // When - create new viewModel to trigger the flow collection
        val newViewModel = HomeViewModel(
            getProductsUseCase,
            addToCartUseCase,
            toggleFavoriteUseCase,
            favoriteRepository
        )
        advanceUntilIdle()

        // Then
        assertEquals(favoriteIds, newViewModel.favoriteIds.getOrAwaitValue())
    }
}