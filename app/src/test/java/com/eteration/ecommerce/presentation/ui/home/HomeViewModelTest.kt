package com.eteration.ecommerce.presentation.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eteration.ecommerce.TestUtils
import com.eteration.ecommerce.domain.model.Filter
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.domain.usecase.AddToCartUseCase
import com.eteration.ecommerce.domain.usecase.GetProductsUseCase
import com.eteration.ecommerce.domain.usecase.ToggleFavoriteUseCase
import com.eteration.ecommerce.domain.repository.FavoriteRepository
import com.eteration.ecommerce.getOrAwaitValue
import com.eteration.ecommerce.presentation.utils.ViewState
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class HomeViewModelTest {

}