package com.eteration.ecommerce.presentation.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eteration.ecommerce.MainDispatcherRule
import com.eteration.ecommerce.domain.repository.CartRepository
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
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        cartRepository = mock()
    }

    @Test
    fun `cartItemCount should expose cart item count from repository`() = runTest {
        // Given
        val expectedCount = 5
        whenever(cartRepository.getCartItemCount()).thenReturn(flowOf(expectedCount))

        // When
        viewModel = MainViewModel(cartRepository)
        advanceUntilIdle()

        // Then
        val cartItemCount = viewModel.cartItemCount.getOrAwaitValue()
        assertEquals(expectedCount, cartItemCount)
    }

    @Test
    fun `cartItemCount should handle zero count`() = runTest {
        // Given
        val zeroCount = 0
        whenever(cartRepository.getCartItemCount()).thenReturn(flowOf(zeroCount))

        // When
        viewModel = MainViewModel(cartRepository)
        advanceUntilIdle()

        // Then
        val cartItemCount = viewModel.cartItemCount.getOrAwaitValue()
        assertEquals(zeroCount, cartItemCount)
    }

    @Test
    fun `cartItemCount should handle large numbers`() = runTest {
        // Given
        val largeCount = 999
        whenever(cartRepository.getCartItemCount()).thenReturn(flowOf(largeCount))

        // When
        viewModel = MainViewModel(cartRepository)
        advanceUntilIdle()

        // Then
        val cartItemCount = viewModel.cartItemCount.getOrAwaitValue()
        assertEquals(largeCount, cartItemCount)
    }
}