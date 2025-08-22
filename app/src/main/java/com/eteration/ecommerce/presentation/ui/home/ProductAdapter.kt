package com.eteration.ecommerce.presentation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eteration.ecommerce.R
import com.eteration.ecommerce.domain.model.Product
import com.eteration.ecommerce.presentation.utils.loadImage

/**
 * RecyclerView adapter for displaying products
 */
class ProductAdapter(
    private val onItemClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    //TODO: Add favorite logic
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.product_image)
        private val nameText: TextView = itemView.findViewById(R.id.product_name)
        private val priceText: TextView = itemView.findViewById(R.id.product_price)
        private val addToCartButton: Button = itemView.findViewById(R.id.btn_add_to_cart)
        private val favoriteIcon: ImageView = itemView.findViewById(R.id.favorite_icon)

        fun bind(product: Product) {
            nameText.text = product.name
            priceText.text = product.getFormattedPrice()
            imageView.loadImage(product.image)

            // Update favorite icon
            favoriteIcon.setImageResource(
                R.drawable.ic_star_outline
            )

            // Click listeners
            itemView.setOnClickListener {
                onItemClick(product)
            }

        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}