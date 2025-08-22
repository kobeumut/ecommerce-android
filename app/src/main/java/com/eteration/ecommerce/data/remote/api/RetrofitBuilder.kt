package com.eteration.ecommerce.data.remote.api

import com.eteration.ecommerce.presentation.utils.AppConstants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit builder singleton
 */
object RetrofitBuilder {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val productApi: ProductApi by lazy {
        retrofit.create(ProductApi::class.java)
    }
}