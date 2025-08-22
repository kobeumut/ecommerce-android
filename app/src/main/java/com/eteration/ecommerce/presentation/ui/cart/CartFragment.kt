package com.eteration.ecommerce.presentation.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eteration.ecommerce.R
import com.eteration.ecommerce.di.AppContainer
import com.eteration.ecommerce.presentation.utils.gone
import com.eteration.ecommerce.presentation.utils.visible

/**
 * Fragment for displaying shopping cart
 */
class CartFragment : Fragment() {

    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    // Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalPriceText: TextView
    private lateinit var completeButton: Button
    private lateinit var emptyText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupViewModel()
        setupRecyclerView()
        setupCompleteButton()
        observeViewModel()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.cart_recycler_view)
        totalPriceText = view.findViewById(R.id.total_price_text)
        completeButton = view.findViewById(R.id.btn_complete)
        emptyText = view.findViewById(R.id.empty_text)
        progressBar = view.findViewById(R.id.progress_bar)
    }

    private fun setupViewModel() {
        // Use dependency injection
        val appContainer = AppContainer.getInstance(requireContext())
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(appContainer.cartRepository) as T
            }
        })[CartViewModel::class.java]
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onIncreaseClick = { cartItem ->
                viewModel.increaseQuantity(cartItem)
            },
            onDecreaseClick = { cartItem ->
                viewModel.decreaseQuantity(cartItem)
            }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
    }

    private fun setupCompleteButton() {
        completeButton.setOnClickListener {
            viewModel.completeOrder()
        }
    }

    private fun observeViewModel() {
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                emptyText.visible()
                recyclerView.gone()
                completeButton.isEnabled = false
            } else {
                emptyText.gone()
                recyclerView.visible()
                completeButton.isEnabled = true
                cartAdapter.submitList(items)
            }
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            totalPriceText.text = viewModel.getFormattedPrice(total)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressBar.visible()
                completeButton.isEnabled = false
            } else {
                progressBar.gone()
                completeButton.isEnabled = true
            }
        }
    }


}