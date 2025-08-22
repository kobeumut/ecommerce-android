package com.eteration.ecommerce.data.mapper

import com.eteration.ecommerce.data.local.entity.CartItemEntity
import com.eteration.ecommerce.data.remote.model.ProductResponse
import com.eteration.ecommerce.domain.model.CartItem
import com.eteration.ecommerce.domain.model.Product

/**
 * Mapper functions to convert between data and domain models
 */


fun CartItemEntity.toDomainModel(): CartItem {
    return CartItem(
        id = id,
        productId = productId,
        productName = productName,
        productImage = productImage,
        price = price,
        quantity = quantity
    )
}

fun CartItem.toEntity(): CartItemEntity {
    return CartItemEntity(
        id = id,
        productId = productId,
        productName = productName,
        productImage = productImage,
        price = price,
        quantity = quantity
    )
}

fun ProductResponse.toDomainModel(): Product {
    return Product(
        id = id,
        name = name,
        image = image,
        price = price.toDoubleOrNull() ?: 0.0,
        description = description,
        model = model,
        brand = brand,
        createdAt = createdAt
    )
}

fun Product.toCartItemEntity(quantity: Int = 1): CartItemEntity {
    return CartItemEntity(
        productId = id,
        productName = name,
        productImage = image,
        price = price,
        quantity = quantity
    )
}
