package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDataClass(
    var userid: String = "",
    var item: ArrayList<CartDataClass> = ArrayList(),
    var address: AddressDataClass = AddressDataClass(),
    var title: String = "",
    var subtotal: String = "",
    var shipingCharge: String = "",
    var totalAmount: String = "",
    var cartImage:String="",
    var orderDate:Long=0L,
    var id: String = ""
):Parcelable