package com.eteration.ecommerce.presentation.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eteration.ecommerce.R
import com.eteration.ecommerce.di.AppContainer
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.presentation.ui.home.HomeFragmentDirections
import com.eteration.ecommerce.presentation.ui.home.ProductAdapter
import com.eteration.ecommerce.presentation.utils.gone
import com.eteration.ecommerce.presentation.utils.showToast
import com.eteration.ecommerce.presentation.utils.visible

/**
 * Fragment for displaying favorite products
 */
class FavoritesFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var productAdapter: ProductAdapter

    // Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupViewModel()
        setupRecyclerView()
        observeViewModel()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.favorites_recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)
        emptyText = view.findViewById(R.id.empty_text)
    }

    private fun setupViewModel() {
        // Use dependency injection
        val appContainer = AppContainer.getInstance(requireContext())
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FavoritesViewModel(
                    appContainer.favoriteRepository,
                    appContainer.addToCartUseCase,
                    appContainer.toggleFavoriteUseCase
                ) as T
            }
        })[FavoritesViewModel::class.java]
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onItemClick = { product ->
            },
            onAddToCart = { product ->
                viewModel.addToCart(product)
            },
            onFavoriteClick = { product ->
                viewModel.toggleFavorite(product)
            }
        )

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.favoriteProducts.observe(viewLifecycleOwner) { products ->
            productAdapter.updateFavorites(products.map { product -> product.id })
            progressBar.gone()

            if (products.isEmpty()) {
                emptyText.visible()
                recyclerView.gone()
            } else {
                emptyText.gone()
                recyclerView.visible()
                productAdapter.submitList(products)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressBar.visible()
                emptyText.gone()
                recyclerView.gone()
            } else {
                progressBar.gone()
            }
        }

        viewModel.cartAddedEvent.observe(viewLifecycleOwner) { message ->
            showToast(message)
        }
    }

    private fun navigateToDetail(product: Product) {
        val action = FavoritesFragmentDirections.actionFavToDetail(product)
        findNavController().navigate(action)
    }
}