package com.eteration.ecommerce.data.local.dao

import androidx.room.*
import com.eteration.ecommerce.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for cart operations
 */
@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items ORDER BY timestamp DESC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItemByProductId(productId: String): CartItemEntity?

    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartItemCount(): Flow<Int>

    @Query("SELECT SUM(price * quantity) FROM cart_items")
    fun getTotalPrice(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItemByProductId(productId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateQuantity(productId: String, quantity: Int)
}