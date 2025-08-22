package com.eteration.ecommerce.data.repository

import com.eteration.ecommerce.data.mapper.toDomainModel
import com.eteration.ecommerce.data.remote.api.ProductApi
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of ProductRepository
 */
class ProductRepositoryImpl(
    private val productApi: ProductApi
) : ProductRepository {

    private var cachedProducts: List<Product>? = null

    override suspend fun getProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = productApi.getProducts()
            val products = response.map { it.toDomainModel() }
            cachedProducts = products
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: String): Result<Product?> = withContext(Dispatchers.IO) {
        try {
            // First check cache
            cachedProducts?.find { it.id == id }?.let {
                return@withContext Result.success(it)
            }

            // If not in cache, fetch from API
            val response = productApi.getProducts()
            val products = response.map { it.toDomainModel() }
            cachedProducts = products
            val product = products.find { it.id == id }
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}