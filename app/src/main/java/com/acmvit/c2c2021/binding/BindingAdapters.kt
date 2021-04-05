package com.acmvit.c2c2021.binding

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.acmvit.c2c2021.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter(
    value = ["imageUrl", "placeholder", "circleCrop", "corners", "crossFade"],
    requireAll = false
)
fun loadImage(
    view: ImageView,
    url: String?,
    placeholder: Drawable?,
    circleCrop: Boolean,
    corners: Int?,
    crossFade: Boolean?
) {
    var requestBuilder: RequestBuilder<Bitmap?> = Glide
        .with(view.getContext())
        .asBitmap()
        .load(url)
    if (placeholder != null) {
        requestBuilder = requestBuilder.placeholder(placeholder)
    }

    if (crossFade != null) {
        requestBuilder = requestBuilder.transition(BitmapTransitionOptions().crossFade())
    }

    if (circleCrop) {
        requestBuilder = requestBuilder.circleCrop()
    } else if (corners != null) {
        requestBuilder = requestBuilder.transform(
            RoundedCorners(corners)
        )
    }
    requestBuilder.into(view)
}

@BindingAdapter("isVisible")
fun setVisibility(v: View, isVisible: Boolean) {
    v.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("isGone")
fun setVisibilityGone(v: View, isGone: Boolean) {
    v.visibility = if (isGone) View.GONE else View.VISIBLE
}

@BindingAdapter("isDrawableVisble")
fun setDrawableVisibility(fab: FloatingActionButton, isVisible: Boolean) {
    fab.setImageDrawable(ColorDrawable(fab.context?.let { it1 ->
        ContextCompat.getColor(it1, android.R.color.transparent) } ?: 0 ))
}
