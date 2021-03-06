package com.example.myshop.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshop.R
import de.hdodenhof.circleimageview.CircleImageView

object ConstVal {


    const val putExteraChatDetail: String ="userDetailSenderMessage"
    const val putExteraUserIdSeller: String ="userIdSeller"
    const val putExteraUserSellerProfileImageUri: String="userSellerImage"
    val fristLoginYes: String="firstLoginyes"


    //SharePrefName
    val fristLogin: String?="firstLogin"
    val Language: String?="yourLanques"

    //collections Name
    const val Collection_Users = "Users"
    const val collection_address="address"
    const val Collection_product = "product"
    const val Collection_SoldProduct="Sold_Product"
    const val Collection_Order="order"
    const val collection_cart="cart"


   //Put Extera Detail
    const val PutExtra_UserDetail = "Userdetail"
    const val PutExtra_OrderDetail="profImge"

    //
    const val user_id="user_id"
    const val user_id_seller="user_id_Seller"
    const val user_id_buyer="user_id_buyer"

    const val product_id="product_Id"


    const val MySharePref = "MYSharePref"
    const val UserNameKeyPref = "username"

    //request Code
    const val requestCode_AccessLocation=1008
    const val RequestCode_Gallery = 1002
    const val RequestCode_Permission = 1003
    const val ActivityStartCode_selectAddress =1004
    const val RequestCode_Pick_Video_from_Gallery=1005
    const val RequestCode_Pick_Video_from_camera=1006
    val googleLogin: Int =1007

    const val Male = "Male"
    const val phonenumber = "mobile"
    const val name = "firstName"
    const val Female = "Female"
    const val gender = "gender"
    const val Lastname = "lastName"
    const val UserProfileImage = "User_Profile_Image"
    const val AddProductImage = "Add_Product_Image"
    const val AddProductVideo = "Add_Product_Video"


    const val imageColumn: String = "image"
    const val usercompelete = "profile_Compelete"


    const val putExtera_detail_product = "detail"
    const val PutExtera_detail_userid = "userid"
    const val cart_quantity:Int = 1


    const val cart_quantity_colmn="card_quantity"
    const val userImageuri="user_image"
    const val Home="home"
    const val office="office"
    const val other="other"

    const val addressDetailExtera="exteraDetailAddress"
    const val pushExtera_selecet_address="selectedAddress"

    const val putExteraDetailAddressTocheckout="exteraDetailAddress"




    const val putExteraSolddetail: String="soldDetail"



    fun ChoseImageFromGallery(activity: Activity) {
        val GalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(GalleryIntent, RequestCode_Gallery)
    }
    fun choseVideoFromgallery(activity: Activity)
    {
        var videointent=Intent()
        videointent.type="video/*"
        videointent.action=Intent.ACTION_GET_CONTENT
        activity.startActivityForResult(videointent,RequestCode_Pick_Video_from_Gallery)

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
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(uri?.let { activity.contentResolver.getType(it) })!!
    }





}