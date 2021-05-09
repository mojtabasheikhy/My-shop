package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDataClass(
    var id: String = "",
    var FirstName: String = "",
    var product_title: String = "",
    var product_pricce: Float = 0f,
    var product_desc: String = "",
    var product_quantity: Int = 0,
    var product_image: String = "",
    var product_id: String = ""

):Parcelable