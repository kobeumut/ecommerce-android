package com.eteration.ecommerce.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.eteration.ecommerce.R
import com.eteration.ecommerce.di.AppContainer
import com.eteration.ecommerce.presentation.utils.gone
import com.eteration.ecommerce.presentation.utils.loadImage
import com.eteration.ecommerce.presentation.utils.visible

/**
 * Fragment for displaying product details
 */
class ProductDetailFragment : Fragment() {

    private lateinit var viewModel: ProductDetailViewModel
    private val args: ProductDetailFragmentArgs by navArgs()

    // Views
    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var productTitle: TextView
    private lateinit var productDescription: TextView
    private lateinit var productPrice: TextView
    private lateinit var favoriteIcon: ImageView
    private lateinit var addToCartButton: Button
    private lateinit var backButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupViewModel()
        bindProductData()
        setupFavoriteIcon()
        setupButtons()
        observeViewModel()
    }

    private fun initViews(view: View) {
        productImage = view.findViewById(R.id.product_image)
        productName = view.findViewById(R.id.product_name)
        productTitle = view.findViewById(R.id.product_name_display)
        productDescription = view.findViewById(R.id.product_description)
        productPrice = view.findViewById(R.id.product_price)
        favoriteIcon = view.findViewById(R.id.favorite_icon)
        addToCartButton = view.findViewById(R.id.btn_add_to_cart)
        backButton = view.findViewById(R.id.back_button)
    }

    private fun setupViewModel() {
        // Use dependency injection
        val appContainer = AppContainer.getInstance(requireContext())
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ProductDetailViewModel(
                    appContainer.addToCartUseCase,
                    appContainer.toggleFavoriteUseCase,
                    appContainer.favoriteRepository
                ) as T
            }
        })[ProductDetailViewModel::class.java]
    }

    private fun bindProductData() {
        val product = args.product
        productImage.loadImage(product.image)
        val maxNameLength = 60
        val name = if (product.name.length > maxNameLength) {
            "${product.name.take(maxNameLength)}..."
        } else {
            product.name
        }
        productName.text = name
        productTitle.text = name

        productDescription.text = product.description
        productPrice.text = product.getFormattedPrice()

        // Check favorite status
        viewModel.checkFavoriteStatus(product.id)
    }

    private fun setupFavoriteIcon() {
        favoriteIcon.setOnClickListener {
            viewModel.toggleFavorite(args.product)
        }
    }

    private fun setupButtons() {
        addToCartButton.setOnClickListener {
            viewModel.addToCart(args.product)
        }
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                favoriteIcon.setImageResource(R.drawable.ic_star_filled)
                favoriteIcon.visible()
            } else {
                favoriteIcon.setImageResource(R.drawable.ic_star_outline)
                favoriteIcon.visible()
            }
        }

        viewModel.cartAddedEvent.observe(viewLifecycleOwner) { message ->
            // Show toast or snackbar
        }
    }
}