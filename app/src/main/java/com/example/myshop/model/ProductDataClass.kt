package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDataClass(
    var user_id_Seller: String = "",
    var profImge_Seller: String = "",
    var FirstName_Seller: String = "",
    var product_title: String = "",
    var product_price: Int = 0,
    var product_desc: String = "",
    var product_quantity: Int = 1,
    var product_image: String = "",
    var product_id: String = "",
    ) : Parcelable