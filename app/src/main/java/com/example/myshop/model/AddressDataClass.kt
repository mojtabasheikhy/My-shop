package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressDataClass (
    var fullname:String="" ,
    var id:String="",
    var phonenumber:Int =0,
    var address:String="" ,
    var otherDetail:String="",
    var additional_note:String="",
    var zipcode:Int=0,
    var typeAddress:String="",
    var address_id:String="",
    ):Parcelable