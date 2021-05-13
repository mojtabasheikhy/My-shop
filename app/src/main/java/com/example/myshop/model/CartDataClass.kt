package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartDataClass(
    var id: String = "",
    var Productid: String = "",
    var title: String = "",
    var price: Int = 0,
    var Image: String = "",
    var productQuantity:String ="",
    var card_quantity: String = "",
    var cart_id: String = ""
):Parcelable