package com.eteration.ecommerce.domain.repository

import com.eteration.ecommerce.domain.model.Product

/**
 * Repository interface for product operations
 */
interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductById(id: String): Result<Product?>
}