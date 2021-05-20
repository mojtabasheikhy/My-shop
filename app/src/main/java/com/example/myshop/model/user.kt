package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class user(
    var user_id: String = "",
    var FirstName: String = "",
    var lastName: String = "",
    var Email: String = "",
    var Mobile: String= "",
    var gender: String = "",
    var profile_Compelete: Int = 0,
    var Image:String =""
): Parcelable