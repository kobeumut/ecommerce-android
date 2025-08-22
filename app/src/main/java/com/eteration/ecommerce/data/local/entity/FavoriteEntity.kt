package com.eteration.ecommerce.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for favorite products
 */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val productId: String,
    val productName: String,
    val productImage: String,
    val price: Double,
    val description: String,
    val model: String,
    val brand: String,
    val timestamp: Long = System.currentTimeMillis()
)