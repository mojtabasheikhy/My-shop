package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartDataClass(
    var user_id_buyer: String = "",
    var User_id_Seller: String = "",
    var product_Id:String="",
    var title: String = "",
    var price: Int = 0,
    var Image_product: String = "",
    var product_Quantity:Int =1,
    var card_quantity: Int = 1,
    var cart_id: String = ""
):Parcelable