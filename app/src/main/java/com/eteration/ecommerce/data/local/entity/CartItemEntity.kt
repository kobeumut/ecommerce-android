package com.eteration.ecommerce.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eteration.ecommerce.presentation.utils.AppConstants.CART_ITEMS


/**
 * Room entity for cart items
 */
@Entity(tableName = CART_ITEMS)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: String,
    val productName: String,
    val productImage: String,
    val price: Double,
    val quantity: Int,
    val timestamp: Long = System.currentTimeMillis()
)