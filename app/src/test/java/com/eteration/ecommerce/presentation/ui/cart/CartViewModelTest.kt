package com.eteration.ecommerce.presentation.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eteration.ecommerce.MainDispatcherRule
import com.eteration.ecommerce.TestUtils
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
class CartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        cartRepository = mock()

        // Setup default mocks
        whenever(cartRepository.getAllCartItems()).thenReturn(flowOf(TestUtils.sampleCartItems))
        whenever(cartRepository.getTotalPrice()).thenReturn(flowOf(27000.0))

        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `init should observe cart items and total price`() = runTest {
        // Given - setup is done in @Before
        advanceUntilIdle()

        // Then
        val cartItems = viewModel.cartItems.getOrAwaitValue()
        assertEquals(TestUtils.sampleCartItems, cartItems)

        val totalPrice = viewModel.totalPrice.getOrAwaitValue()
        assertEquals(27000.0, totalPrice, 0.01)
    }

    @Test
    fun `updateQuantity should call repository with correct parameters`() = runTest {
        // Given
        val productId = "1"
        val quantity = 3
        whenever(cartRepository.updateQuantity(any(), any())).thenReturn(Unit)

        // When
        viewModel.updateQuantity(productId, quantity)
        advanceUntilIdle()

        // Then
        verify(cartRepository).updateQuantity(productId, quantity)
    }

    @Test
    fun `increaseQuantity should call updateQuantity with incremented value`() = runTest {
        // Given
        val cartItem = TestUtils.sampleCartItems[0] // quantity = 2
        whenever(cartRepository.updateQuantity(any(), any())).thenReturn(Unit)

        // When
        viewModel.increaseQuantity(cartItem)
        advanceUntilIdle()

        // Then
        verify(cartRepository).updateQuantity(cartItem.productId, 3)
    }

    @Test
    fun `decreaseQuantity should call updateQuantity with decremented value when quantity greater than 1`() = runTest {
        // Given
        val cartItem = TestUtils.sampleCartItems[0] // quantity = 2
        whenever(cartRepository.updateQuantity(any(), any())).thenReturn(Unit)

        // When
        viewModel.decreaseQuantity(cartItem)
        advanceUntilIdle()

        // Then
        verify(cartRepository).updateQuantity(cartItem.productId, 1)
        verify(cartRepository, never()).removeFromCart(any())
    }

    @Test
    fun `decreaseQuantity should call removeFromCart when quantity equals 1`() = runTest {
        // Given
        val cartItem = TestUtils.sampleCartItems[1].copy(quantity = 1)
        whenever(cartRepository.removeFromCart(any())).thenReturn(Unit)

        // When
        viewModel.decreaseQuantity(cartItem)
        advanceUntilIdle()

        // Then
        verify(cartRepository).removeFromCart(cartItem.productId)
        verify(cartRepository, never()).updateQuantity(any(), any())
    }

    @Test
    fun `removeItem should call repository removeFromCart`() = runTest {
        // Given
        val productId = "1"
        whenever(cartRepository.removeFromCart(any())).thenReturn(Unit)

        // When
        viewModel.removeItem(productId)
        advanceUntilIdle()

        // Then
        verify(cartRepository).removeFromCart(productId)
    }

    @Test
    fun `clearCart should call repository clearCart`() = runTest {
        // Given
        whenever(cartRepository.clearCart()).thenReturn(Unit)

        // When
        viewModel.clearCart()
        advanceUntilIdle()

        // Then
        verify(cartRepository).clearCart()
    }

    @Test
    fun `completeOrder should set loading state and clear cart`() = runTest {
        // Given
        whenever(cartRepository.clearCart()).thenReturn(Unit)

        // When
        viewModel.completeOrder()

        // Then - check loading state is set to true initially
        assertTrue(viewModel.isLoading.getOrAwaitValue())

        // Advance time to complete the delay
        advanceUntilIdle()

        // Then - verify cart is cleared and loading is false
        verify(cartRepository).clearCart()
        assertFalse(viewModel.isLoading.getOrAwaitValue())
    }

    @Test
    fun `getFormattedPrice should return properly formatted price string`() {
        // Given
        val price = 1234.56

        // When
        val formattedPrice = viewModel.getFormattedPrice(price)

        // Then
        assertEquals("1234.56 ₺", formattedPrice)
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