package com.eteration.ecommerce.domain.usecase

import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.domain.model.Filter
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.model.SortType
import com.eteration.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class GetProductsUseCaseTest {

    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var productRepository: ProductRepository

    @Before
    fun setUp() {
        productRepository = mock()
        getProductsUseCase = GetProductsUseCase(productRepository)
    }


    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        whenever(productRepository.getProducts()).thenReturn(Result.failure(exception))

        // When
        val result = getProductsUseCase.invoke().first()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(productRepository).getProducts()
    }

    @Test
    fun `invoke should filter products by search query`() = runTest {
        // Given
        val products = TestUtils.sampleProducts
        whenever(productRepository.getProducts()).thenReturn(Result.success(products))

        // When
        val result = getProductsUseCase.invoke(searchQuery = "iPhone").first()

        // Then
        assertTrue(result.isSuccess)
        val filteredProducts = result.getOrNull()
        assertNotNull(filteredProducts)
        assertEquals(1, filteredProducts?.size)
        assertEquals("iPhone 13", filteredProducts?.get(0)?.model)
    }

    @Test
    fun `invoke should filter products by brand`() = runTest {
        // Given
        val products = TestUtils.sampleProducts
        whenever(productRepository.getProducts()).thenReturn(Result.success(products))

        // When
        val filter = Filter(selectedBrands = setOf("Samsung"))
        val result = getProductsUseCase.invoke(filter = filter).first()

        // Then
        assertTrue(result.isSuccess)
        val filteredProducts = result.getOrNull()
        assertNotNull(filteredProducts)
        assertEquals(1, filteredProducts?.size)
        assertEquals("Samsung", filteredProducts?.get(0)?.brand)
    }

    @Test
    fun `invoke should sort products by price low to high`() = runTest {
        // Given
        val products = TestUtils.sampleProducts
        whenever(productRepository.getProducts()).thenReturn(Result.success(products))

        // When
        val filter = Filter(sortBy = SortType.PRICE_LOW_TO_HIGH)
        val result = getProductsUseCase.invoke(filter = filter).first()

        // Then
        assertTrue(result.isSuccess)
        val sortedProducts = result.getOrNull()
        assertNotNull(sortedProducts)
        assertEquals(3, sortedProducts?.size)
        assertEquals("Huawei", sortedProducts?.get(0)?.brand) // Lowest price
        assertEquals("Samsung", sortedProducts?.get(1)?.brand) // Middle price
        assertEquals("Apple", sortedProducts?.get(2)?.brand) // Highest price
    }

    @Test
    fun `invoke should sort products by price high to low`() = runTest {
        // Given
        val products = TestUtils.sampleProducts
        whenever(productRepository.getProducts()).thenReturn(Result.success(products))

        // When
        val filter = Filter(sortBy = SortType.PRICE_HIGH_TO_LOW)
        val result = getProductsUseCase.invoke(filter = filter).first()

        // Then
        assertTrue(result.isSuccess)
        val sortedProducts = result.getOrNull()
        assertNotNull(sortedProducts)
        assertEquals(3, sortedProducts?.size)
        assertEquals("Apple", sortedProducts?.get(0)?.brand) // Highest price
        assertEquals("Samsung", sortedProducts?.get(1)?.brand) // Middle price
        assertEquals("Huawei", sortedProducts?.get(2)?.brand) // Lowest price
    }
}