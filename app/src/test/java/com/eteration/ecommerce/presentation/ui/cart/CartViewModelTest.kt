package com.eteration.ecommerce.presentation.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.domain.model.CartItem
import com.eteration.ecommerce.domain.repository.CartRepository
import com.eteration.ecommerce.getOrAwaitValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class CartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        cartRepository = mock()
        
        // Mock repository to return empty flows by default
        whenever(cartRepository.getAllCartItems()).thenReturn(flowOf(emptyList()))
        whenever(cartRepository.getTotalPrice()).thenReturn(flowOf(0.0))
        
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `getFormattedPrice should format price correctly`() {
        // Given
        val price = 1234.567

        // When
        val formattedPrice = viewModel.getFormattedPrice(price)

        // Then
        assertEquals("1234.57 ₺", formattedPrice)
    }

    @Test
    fun `getFormattedPrice should handle zero price`() {
        // Given
        val price = 0.0

        // When
        val formattedPrice = viewModel.getFormattedPrice(price)

        // Then
        assertEquals("0.00 ₺", formattedPrice)
    }
}