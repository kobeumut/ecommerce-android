package com.eteration.ecommerce.data.remote.api

import com.eteration.ecommerce.data.remote.model.ProductResponse
import retrofit2.http.GET

/**
 * Retrofit API interface for product endpoints
 */
interface ProductApi {

    @GET("products")
    suspend fun getProducts(): List<ProductResponse>
}