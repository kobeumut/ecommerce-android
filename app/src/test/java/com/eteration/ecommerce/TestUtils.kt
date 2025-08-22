package com.eteration.ecommerce

import com.eteration.ecommerce.domain.model.CartItem
import com.eteration.ecommerce.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

object TestUtils {
    // Sample test data
    val sampleProducts = listOf(
        Product(
            id = "1",
            brand = "Apple",
            model = "iPhone 13",
            price = 15000.0,
            name = "Apple iPhone 13",
            image = "https://example.com/image1.jpg",
            description = "Lorem ipsum dolor sit amet",
            createdAt = "2023-07-17T01:24:59.268Z"
        ),
        Product(
            id = "2",
            brand = "Samsung",
            model = "Galaxy S22",
            price = 12000.0,
            name = "Samsung Galaxy S22",
            image = "https://example.com/image2.jpg",
            description = "Lorem ipsum dolor sit amet",
            createdAt = "2023-08-17T01:24:59.268Z"

        ),
        Product(
            id = "3",
            brand = "Huawei",
            model = "P50",
            price = 10000.0,
            name = "Huawei P50",
            image = "https://example.com/image5.jpg",
            description = "Lorem ipsum dolor sit amet",
            createdAt = "2023-08-01T01:24:59.268Z"
        )
    )

    val sampleCartItems = listOf(
        CartItem(
            quantity = 2,
            id = 1,
            productId = "1",
            productName = "Apple iPhone 13",
            productImage = "https://example.com/image1.jpg",
            price = 15000.0,
        ),
        CartItem(
            quantity = 1,
            id = 2,
            productId = sampleProducts[1].id,
            productName = sampleProducts[1].name,
            productImage = sampleProducts[1].image,
            price = sampleProducts[1].price
        ),
    )

    // Helper function to create a flow with a value
    fun <T> flowWith(value: T): Flow<T> = flowOf(value)

    // Helper function to create a flow with an error
    fun <T> flowWithError(error: Throwable): Flow<T> = flow {
        throw error
    }
}