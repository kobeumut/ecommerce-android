package com.eteration.ecommerce.presentation.utils

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

/**
 * Extension functions for UI components
 */

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .placeholder(android.R.drawable.ic_menu_report_image)
        .centerCrop()
        .into(this)
}