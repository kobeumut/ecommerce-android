package com.eteration.ecommerce.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.usecase.GetProductsUseCase

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
) : ViewModel() {

    private val _productsState = MutableLiveData<ViewState<List<Product>>>(ViewState.Loading)
    val productsState: LiveData<ViewState<List<Product>>> = _productsState

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery


    private var searchJob: Job? = null

    init {
        loadProducts()
    }


    fun loadProducts() {
        viewModelScope.launch {
            _productsState.postValue(ViewState.Loading)

            getProductsUseCase(
                searchQuery = _searchQuery.value ?: ""
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

        // Debounce
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            loadProducts()
        }
    }


//TODO: Should add Favorite, addtocart, filter

    fun retry() {
        loadProducts()
    }
}