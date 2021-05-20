package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDataClass(
    var user_id_buyer: String = "",
    var cartitem: ArrayList<CartDataClass> = ArrayList(),
    var address_buyer: AddressDataClass = AddressDataClass(),
    var title: String = "",
    var subtotal: String = "",
    var shipingCharge: String = "",
    var totalAmount: String = "",
    var cartImage:String="",
    var orderDate:Long=0L,
    var order_id: String = ""
):Parcelable