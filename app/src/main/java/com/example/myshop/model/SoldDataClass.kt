package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SoldDataClass (
    var product_id:String="",
    var title_product:String="" ,
    var Price:String="" ,
    var sold_quantity:String="",
    var image_product:String="",
    var order_date:Long=0L,
    var subtotal_amount:String="",
    var shiping_charge:String="",
    var totalAmount:String="",
    var Address_user_buyed:AddressDataClass=AddressDataClass(),
    var user_id_Seller:String="",
    var order_id:String=""

):Parcelable
