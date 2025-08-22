package com.eteration.ecommerce.data.repository

import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.data.remote.api.ProductApi
import com.eteration.ecommerce.data.remote.model.ProductResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.*

class ProductRepositoryImplTest {

    private lateinit var productRepository: ProductRepositoryImpl
    private lateinit var productApi: ProductApi

    @Before
    fun setUp() {
        productApi = mock()
        productRepository = ProductRepositoryImpl(productApi)
    }

    @Test
    fun `getProducts should return success when API call succeeds`() = runTest {
        // Given
        val productResponses = listOf(
            ProductResponse(
                id = "1",
                brand = "Apple",
                model = "iPhone 13",
                price = "15000.0",
                image = "https://example.com/iphone13.jpg",
                name = "Apple iPhone 13",
                description = "Lorem ipsum",
                createdAt = "2023-07-17T01:24:59.268Z"

            )
        )
        whenever(productApi.getProducts()).thenReturn(productResponses)

        // When
        val result = productRepository.getProducts()

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        verify(productApi).getProducts()
    }

    @Test
    fun `getProducts should return failure when API call fails`() = runTest {
        // Given
        val exception = MockitoException("Network error")
        whenever(productApi.getProducts()).thenThrow(exception)

        // When
        val result = productRepository.getProducts()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(productApi).getProducts()
    }

    @Test
    fun `getProductById should return product when found in cache`() = runTest {
        // Given
        val products = TestUtils.sampleProducts
        val targetProduct = products[0]
        
        // First call to populate cache
        whenever(productApi.getProducts()).thenReturn(
            products.map {
                ProductResponse(
                    id = it.id,
                    brand = it.brand,
                    model = it.model,
                    price = it.price.toString(),
                    name = it.name,
                    description = it.description,
                    createdAt = it.createdAt,
                    image = it.image
                )
            }
        )
        
        // Populate cache
        productRepository.getProducts()
        
        // When
        val result = productRepository.getProductById(targetProduct.id)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(targetProduct, result.getOrNull())
    }

    @Test
    fun `getProductById should fetch from API when not in cache`() = runTest {
        // Given
        val products = TestUtils.sampleProducts
        val targetProduct = products[0]
        
        whenever(productApi.getProducts()).thenReturn(
            products.map {
                ProductResponse(
                    id = it.id,
                    brand = it.brand,
                    model = it.model,
                    price = it.price.toString(),
                    name = it.name,
                    description = it.description,
                    createdAt = it.createdAt,
                    image = it.image
                )
            }
        )
        
        // When
        val result = productRepository.getProductById(targetProduct.id)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(targetProduct, result.getOrNull())
        verify(productApi).getProducts()
    }

    @Test
    fun `getProductById should return failure when API call fails`() = runTest {
        // Given
        val exception = MockitoException("Network error")
        whenever(productApi.getProducts()).thenThrow(exception)
        
        // When
        val result = productRepository.getProductById("1")

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}