package com.eteration.ecommerce.presentation.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import com.eteration.ecommerce.presentation.utils.ViewState
import com.eteration.ecommerce.presentation.utils.gone
import com.eteration.ecommerce.presentation.utils.showToast
import com.eteration.ecommerce.presentation.utils.visible

/**
 * Fragment for displaying product listing
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var productAdapter: ProductAdapter

    // Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var filterButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var emptyText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupViewModel()
        setupRecyclerView()
        setupSearchBar()
        observeViewModel()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.products_recycler_view)
        searchEditText = view.findViewById(R.id.search_edit_text)
        filterButton = view.findViewById(R.id.filter_button)
        progressBar = view.findViewById(R.id.progress_bar)
        errorText = view.findViewById(R.id.error_text)
        emptyText = view.findViewById(R.id.empty_text)
    }

    private fun setupViewModel() {
        // Use dependency injection
        val appContainer = AppContainer.getInstance(requireContext())
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(
                    appContainer.getProductsUseCase
                ) as T
            }
        })[HomeViewModel::class.java]
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onItemClick = { product ->
                navigateToDetail(product)
            }
        )

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter

            // Infinite scroll
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                        firstVisibleItemPosition >= 0 &&
                        totalItemCount >= 4
                    ) {
                        // Load more items if needed
                    }
                }
            })
        }
    }

    private fun setupSearchBar() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchProducts(s.toString())
            }
        })
    }

    private fun observeViewModel() {
        viewModel.productsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Loading -> {
                    progressBar.visible()
                    errorText.gone()
                    emptyText.gone()
                    recyclerView.gone()
                }
                is ViewState.Success -> {
                    progressBar.gone()
                    errorText.gone()

                    if (state.data.isEmpty()) {
                        emptyText.visible()
                        recyclerView.gone()
                    } else {
                        emptyText.gone()
                        recyclerView.visible()
                        productAdapter.submitList(state.data)
                    }
                }
                is ViewState.Error -> {
                    progressBar.gone()
                    errorText.visible()
                    errorText.text = state.message
                    emptyText.gone()
                    recyclerView.gone()
                }
            }
        }
        
    }

    private fun navigateToDetail(product: Product) {
        val action = HomeFragmentDirections.actionHomeToDetail(product)
        findNavController().navigate(action)
    }
}