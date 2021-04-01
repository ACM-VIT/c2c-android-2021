package com.acmvit.c2c2021.binding

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

@BindingAdapter(
    value = ["imageUrl", "placeholder", "circleCrop", "corners"],
    requireAll = false
)
fun loadImage(
    view: ImageView,
    url: String?,
    placeholder: Drawable?,
    circleCrop: Boolean,
    corners: Int?
) {
    var requestBuilder: RequestBuilder<Bitmap?> = Glide
        .with(view.getContext())
        .asBitmap()
        .load(url)
    if (placeholder != null) {
        requestBuilder = requestBuilder.placeholder(placeholder)
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

