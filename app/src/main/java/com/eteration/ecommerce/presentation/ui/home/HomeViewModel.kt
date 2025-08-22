package com.eteration.ecommerce.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eteration.ecommerce.domain.model.Filter
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.usecase.AddToCartUseCase
import com.eteration.ecommerce.domain.usecase.GetProductsUseCase
import com.eteration.ecommerce.domain.usecase.ToggleFavoriteUseCase
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import com.eteration.ecommerce.presentation.utils.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for Home screen
 */
class HomeViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _productsState = MutableLiveData<ViewState<List<Product>>>(ViewState.Loading)
    val productsState: LiveData<ViewState<List<Product>>> = _productsState

    private val _favoriteIds = MutableLiveData<List<String>>(emptyList())
    val favoriteIds: LiveData<List<String>> = _favoriteIds

    private val _currentFilter = MutableLiveData(Filter())
    val currentFilter: LiveData<Filter> = _currentFilter

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _cartAddedEvent = MutableLiveData<String>()
    val cartAddedEvent: LiveData<String> = _cartAddedEvent

    private var searchJob: Job? = null

    init {
        loadProducts()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getFavoriteIds().collectLatest { ids ->
                _favoriteIds.postValue(ids)
            }
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            _productsState.postValue(ViewState.Loading)

            getProductsUseCase(
                searchQuery = _searchQuery.value ?: "",
                filter = _currentFilter.value ?: Filter()
            ).collectLatest { result ->
                result.fold(
                    onSuccess = { products ->
                        _productsState.postValue(ViewState.Success(products))
                    },
                    onFailure = { error ->
                        _productsState.postValue(
                            ViewState.Error(error.message ?: "Unknown error occurred")
                        )
                    }
                )
            }
        }
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query

        // Debounce search
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            loadProducts()
        }
    }

    fun applyFilter(filter: Filter) {
        _currentFilter.value = filter
        loadProducts()
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
        }
    }

    fun retry() {
        loadProducts()
    }
}