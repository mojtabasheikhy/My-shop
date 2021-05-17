package com.example.myshop.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshop.R
import com.example.myshop.View.activity.AddProduct
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.reflect.Executable

object ConstVal {
    const val Collection_Users = "Users"
    const val MySharePref = "MYSharePref"
    const val UserNameKeyPref = "username"
    const val putExtra_UserDetail = "Userdetail"
    const val RequestCode_Gallery = 1002
    const val RequestCode_Permission = 1003
    const val Male = "Male"
    const val phonenumber = "mobile"
    const val name = "firstName"
    const val Female = "Female"
    const val gender = "gender"
    const val Lastname = "lastName"
    const val UserProfileImage = "User_Profile_Image"
    const val AddProductImage = "Add_Product_Image"
    const val imageColumn: String = "image"
    const val usercompelete = "profile_Compelete"
    const val Collection_addproduct = "product"
    const val UserId = "id"
    const val putExtera_detail_product = "detail"
    const val PutExtera_detail_userid = "userid"
    const val cart_quantity:Int = 1
    const val cart_item_collection="cartItem"
    const val product_id="productid"
    const val cart_quantity_colmn="card_quantity"
    const val userImageuri="user_image"
    const val Home="home"
    const val office="office"
    const val other="other"
    const val address_collection="address"
    const val addressDetailExtera="exteraDetailAddress"
    const val pushExtera_selecet_address="selectedAddress"
    const val ActivityStartCode_selectAddress =1004
    const val putExteraDetailAddressTocheckout="exteraDetailAddress"
    const val OrderCollection="order"
    const val productquantity_doc="product_quantity"
    const val putExteraOrderDetail="profImge"



    fun ChoseImageFromGallery(activity: Activity) {
        val GalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(GalleryIntent, RequestCode_Gallery)
    }

    fun LoadPicByGlide(context: Context, ImageUri: Any, imageView: CircleImageView) {
        try {
            Glide.with(context)
                .load(ImageUri)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()


                .into(imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun LoadPicByGlide_noCircle(context: Context, ImageUri: Any, imageView: ImageView) {
        try {
            Glide.with(context)
                .load(ImageUri)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //knows what is your file type jpg png or svg
    fun GetFileExtention(activity: Activity, uri: Uri?): String {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(uri?.let { activity.contentResolver.getType(it) })!!
    }


}