package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartDataClass(
    var Userid: String = "",
    var Productid: String = "",
    var title: String = "",
    var price: String = "",
    var Image: String = "",
    var productQuantity:String =" ",
    var card_quantity: String = "",
    var id: String = ""
):Parcelable