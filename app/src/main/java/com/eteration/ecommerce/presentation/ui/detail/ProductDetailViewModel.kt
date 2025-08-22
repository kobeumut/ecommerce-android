package com.eteration.ecommerce.presentation.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.usecase.AddToCartUseCase
import com.eteration.ecommerce.domain.usecase.ToggleFavoriteUseCase
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for Product Detail screen
 */
class ProductDetailViewModel(
    private val addToCartUseCase: AddToCartUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _cartAddedEvent = MutableLiveData<String>()
    val cartAddedEvent: LiveData<String> = _cartAddedEvent

    fun checkFavoriteStatus(productId: String) {
        viewModelScope.launch {
            _isFavorite.postValue(favoriteRepository.isFavorite(productId))
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
            _isFavorite.postValue(!(_isFavorite.value ?: false))
        }
    }
}