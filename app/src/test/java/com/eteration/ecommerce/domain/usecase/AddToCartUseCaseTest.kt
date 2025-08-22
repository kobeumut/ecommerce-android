package com.eteration.ecommerce.domain.usecase

import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.domain.repository.CartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.*

class AddToCartUseCaseTest {

    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        cartRepository = mock()
        addToCartUseCase = AddToCartUseCase(cartRepository)
    }

    @Test
    fun `invoke should call repository addToCart with correct product`() = runTest {
        // Given
        val product = TestUtils.sampleProducts[0]

        // When
        addToCartUseCase.invoke(product)

        // Then
        verify(cartRepository).addToCart(product)
    }
}