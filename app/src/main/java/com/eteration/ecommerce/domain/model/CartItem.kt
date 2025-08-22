package com.eteration.ecommerce.domain.model

data class CartItem(
    val id: Long = 0,
    val productId: String,
    val productName: String,
    val productImage: String,
    val price: Double,
    var quantity: Int = 1
) {
    /**
     * Calculate total price for this cart item
     */
    fun getTotalPrice(): Double = price * quantity

    /**
     * Format total price for display
     */
    fun getFormattedTotalPrice(): String {
        return "%.2f ₺".format(getTotalPrice())
    }

    /**
     * Format unit price for display
     */
    fun getFormattedPrice(): String {
        return "%.2f ₺".format(price)
    }
}