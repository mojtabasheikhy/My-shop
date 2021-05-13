package com.example.myshop.bindingadpter

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.bumptech.glide.Glide
import com.example.myshop.R
import de.hdodenhof.circleimageview.CircleImageView

object product_adapterbinding {
    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage_product(view:CircleImageView,url: String){
        view.load(url){
            crossfade(500)
            error(R.drawable.ic_placeholder_product)
            placeholder(R.drawable.ic_product)
            crossfade(500)
        }
    }
    @JvmStatic
    @BindingAdapter("setprice")
    fun chaneFloatToString(textView: TextView,price:Int) {
        textView.setText(price.toString())
    }

    @JvmStatic
    @BindingAdapter("setImageNoCircleImageView")
    fun setImage_product_nocircle(view:ImageView,url: String){
        view.load(url){
            crossfade(500)
            error(R.drawable.ic_placeholder_product)
            placeholder(R.drawable.ic_product)
            crossfade(500)
        }
    }

}