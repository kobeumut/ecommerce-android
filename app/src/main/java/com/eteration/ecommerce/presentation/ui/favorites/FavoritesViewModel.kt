package com.eteration.ecommerce.presentation.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import com.eteration.ecommerce.domain.usecase.AddToCartUseCase
import com.eteration.ecommerce.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for Favorites screen
 */
class FavoritesViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val addToCartUseCase: AddToCartUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _favoriteProducts = MutableLiveData<List<Product>>(emptyList())
    val favoriteProducts: LiveData<List<Product>> = _favoriteProducts

    private val _loading = MutableLiveData<Boolean>(true)
    val loading: LiveData<Boolean> = _loading
    private val _cartAddedEvent = MutableLiveData<String>()
    val cartAddedEvent: LiveData<String> = _cartAddedEvent
    init {
        loadFavoriteProducts()
    }

    private fun loadFavoriteProducts() {
        viewModelScope.launch {
            _loading.postValue(true)
            favoriteRepository.getAllFavorites().collectLatest { products ->
                _favoriteProducts.postValue(products)
                _loading.postValue(false)
            }
        }
    }
    fun addToCart(product: Product) {
        viewModelScope.launch {
            addToCartUseCase(product)
            _cartAddedEvent.postValue("${product.name} added to cart")
        }
    }
    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            toggleFavoriteUseCase(product)
            // Reload favorites after toggle
            loadFavoriteProducts()
        }
    }
}