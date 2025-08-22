package com.eteration.ecommerce.domain.usecase

import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching and filtering products
 */
//TODO: Add Filter and sort
class GetProductsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(
        searchQuery: String = ""
    ): Flow<Result<List<Product>>> = flow {
        productRepository.getProducts().fold(
            onSuccess = { products ->
                val filteredProducts = products
                    .filter { product ->
                        // Search filter
                        (searchQuery.isEmpty() ||
                                product.name.contains(searchQuery, ignoreCase = true))
                    }

                emit(Result.success(filteredProducts))
            },
            onFailure = { error ->
                emit(Result.failure(error))
            }
        )
    }
}