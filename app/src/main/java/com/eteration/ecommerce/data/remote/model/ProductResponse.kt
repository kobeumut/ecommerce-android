package com.eteration.ecommerce.data.remote.model

/**
 * Network response model for Product
 */
data class ProductResponse(
    val id: String,
    val name: String,
    val image: String,
    val price: String,
    val description: String,
    val model: String,
    val brand: String,
    val createdAt: String
)