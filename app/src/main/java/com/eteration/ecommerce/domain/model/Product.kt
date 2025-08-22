package com.eteration.ecommerce.domain.model

import java.io.Serializable

data class Product(
    val id: String,
    val name: String,
    val image: String,
    val price: Double,
    val description: String,
    val model: String,
    val brand: String,
    val createdAt: String
) : Serializable {

    /**
     * Format price for display
     */
    fun getFormattedPrice(): String {
        return "%.2f ₺".format(price)
    }
}