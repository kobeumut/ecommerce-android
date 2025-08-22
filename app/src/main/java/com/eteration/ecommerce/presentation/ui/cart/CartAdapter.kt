package com.eteration.ecommerce.presentation.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eteration.ecommerce.R
import com.eteration.ecommerce.domain.model.CartItem

/**
 * RecyclerView adapter for displaying cart items
 */
class CartAdapter(
    private val onIncreaseClick: (CartItem) -> Unit,
    private val onDecreaseClick: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameText: TextView = itemView.findViewById(R.id.product_name)
        private val priceText: TextView = itemView.findViewById(R.id.product_price)
        private val quantityText: TextView = itemView.findViewById(R.id.quantity_text)
        private val decreaseButton: Button = itemView.findViewById(R.id.btn_decrease)
        private val increaseButton: Button = itemView.findViewById(R.id.btn_increase)

        fun bind(cartItem: CartItem) {
            productNameText.text = cartItem.productName
            priceText.text = cartItem.getFormattedPrice()
            quantityText.text = cartItem.quantity.toString()

            decreaseButton.setOnClickListener {
                onDecreaseClick(cartItem)
            }

            increaseButton.setOnClickListener {
                onIncreaseClick(cartItem)
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}