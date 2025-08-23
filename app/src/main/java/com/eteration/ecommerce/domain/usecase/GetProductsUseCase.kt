package com.eteration.ecommerce.domain.usecase

import com.eteration.ecommerce.domain.model.Filter
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.model.SortType
import com.eteration.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching and filtering products
 */
class GetProductsUseCase(
    private val productRepository: ProductRepository
) {
    operator fun invoke(
        searchQuery: String = "",
        filter: Filter = Filter()
    ): Flow<Result<List<Product>>> = flow {
        productRepository.getProducts().fold(
            onSuccess = { products ->
                val filteredProducts = products
                    .filter { product ->
                        // Search filter
                        (searchQuery.isEmpty() ||
                                product.name.contains(searchQuery, ignoreCase = true))
                    }
                    .filter { product ->
                        // Brand filter
                        filter.selectedBrands.isEmpty() ||
                                filter.selectedBrands.contains(product.brand)
                    }
                    .filter { product ->
                        // Model filter
                        filter.selectedModels.isEmpty() ||
                                filter.selectedModels.contains(product.model)
                    }
                    .sortedWith(
                        when (filter.sortBy) {
                            SortType.OLD_TO_NEW -> compareBy { it.createdAt }
                            SortType.NEW_TO_OLD -> compareByDescending { it.createdAt }
                            SortType.PRICE_LOW_TO_HIGH -> compareBy { it.price }
                            SortType.PRICE_HIGH_TO_LOW -> compareByDescending { it.price }
                        }
                    )
                emit(Result.success(filteredProducts))
            },
            onFailure = { error ->
                emit(Result.failure(error))
            }
        )
    }
}