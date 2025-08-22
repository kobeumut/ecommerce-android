package com.eteration.ecommerce.presentation.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eteration.ecommerce.domain.model.CartItem
import com.eteration.ecommerce.domain.repository.CartRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for Cart screen
 */
class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        observeCartItems()
        observeTotalPrice()
    }
    /**
     * Format total price for display
     */
    fun getFormattedPrice(price:Double): String {
        return "%.2f ₺".format(price)
    }
    private fun observeCartItems() {
        viewModelScope.launch {
            cartRepository.getAllCartItems().collectLatest { items ->
                _cartItems.postValue(items)
            }
        }
    }

    private fun observeTotalPrice() {
        viewModelScope.launch {
            cartRepository.getTotalPrice().collectLatest { total ->
                _totalPrice.postValue(total)
            }
        }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(productId, quantity)
        }
    }

    fun increaseQuantity(cartItem: CartItem) {
        updateQuantity(cartItem.productId, cartItem.quantity + 1)
    }

    fun decreaseQuantity(cartItem: CartItem) {
        if (cartItem.quantity > 1) {
            updateQuantity(cartItem.productId, cartItem.quantity - 1)
        } else {
            removeItem(cartItem.productId)
        }
    }

    fun removeItem(productId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    fun completeOrder() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            // Simulate order processing
            kotlinx.coroutines.delay(2000)
            cartRepository.clearCart()
            _isLoading.postValue(false)
        }
    }
}