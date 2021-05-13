package com.example.myshop.View.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.model.ProductDataClass
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityAddproductBinding
import java.lang.Exception

class AddProduct : Basic(), View.OnClickListener {
    var addproductBinding: ActivityAddproductBinding? = null
    var imageuri_addproduct: Uri? = null
    var downloadAble_Image_uri:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addproductBinding = DataBindingUtil.setContentView(this, R.layout.activity_addproduct)
        actionbar_setup()
        OnClickListener()
    }

    private fun OnClickListener() {
        addproductBinding?.AddproductBtnSend?.setOnClickListener(this)
        addproductBinding?.addproductIvAddpic?.setOnClickListener(this)
    }

    fun actionbar_setup() {
        setSupportActionBar(addproductBinding!!.AddproductToolbar)
        val actionbar_addproduct = supportActionBar
        if (actionbar_addproduct != null) {
            actionbar_addproduct.setDisplayHomeAsUpEnabled(true)
            actionbar_addproduct.title = resources.getString(R.string.add_product)
        }
        addproductBinding!!.AddproductToolbar.setNavigationIcon(R.drawable.ic_back)
        addproductBinding!!.AddproductToolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Addproduct_Btn_Send -> {
               if ( Validation()){
                      SendDataAddProductToFireStore()
               }
            }
            R.id.addproduct_iv_addpic -> {
                checkpermission()
            }
        }
    }

    private fun SendDataAddProductToFireStore() {
        if (imageuri_addproduct!=null){
            ShowDialog(resources.getString(R.string.WaitToUpadteAndSendPic))
            FireStore().UploadImageToCloudStore(this, imageuri_addproduct!!,ConstVal.AddProductImage)

        }
        else
            SendProductDetial()
    }


    fun checkpermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            ConstVal.ChoseImageFromGallery(this)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                ConstVal.RequestCode_Permission
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ConstVal.RequestCode_Permission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ConstVal.ChoseImageFromGallery(this)
            } else {
                ShowSnackbar(resources.getString(R.string.pleaseAllowPermision), false)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ConstVal.RequestCode_Gallery) {

                if (data != null) {
                    try {

                        addproductBinding?.addproductIvAddpic?.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_edit
                            )
                        )

                        imageuri_addproduct = data.data
                        // Compelte_profile.CompleteIvUserprofile.setImageURI(Uri.parse(SelectedImage))
                        imageuri_addproduct?.let {

                            ConstVal.LoadPicByGlide_noCircle(
                                this,
                                it,
                                addproductBinding!!.addproductIvShow
                            )
                            ShowSnackbar(resources.getString(R.string.alerttouploadpic), true)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        ShowSnackbar(resources.getString(R.string.faildSelectedImage), false)

                    }

                }
            }
        }
    }

    fun Validation(): Boolean {
        return when {
            TextUtils.isEmpty(
                addproductBinding?.AddProductTitleEdt?.text?.toString()?.trim { it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.AddTitleProduct), false)
                false
            }
            TextUtils.isEmpty(
                addproductBinding?.AddproductPriceEdt?.text?.toString()?.trim { it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.AddPriceProduct), false)
                false
            }

            TextUtils.isEmpty(
                addproductBinding?.AddproductDescEdt?.text?.toString()?.trim { it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.AddDescProduct), false)
                false
            }
            TextUtils.isEmpty(
                addproductBinding?.AddproductQuantityEdt?.text?.toString()?.trim { it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.AddQuantityProduct), false)
                false
            }

            else -> {
                true
            }
        }
    }
    fun UploadImageSuccess(it: Uri?) {
        /**ارسال ادرس عکس کالا آپلود شده به دیتا بیس*/

        downloadAble_Image_uri = it.toString()
        SendProductDetial()

    }

    private fun SendProductDetial() {
        val title = addproductBinding?.AddProductTitleEdt?.text.toString().trim()
        val Price = addproductBinding?.AddproductPriceEdt?.text.toString().trim()
        val desc =addproductBinding?.AddproductDescEdt?.text.toString().trim()
        val quantity =addproductBinding?.AddproductQuantityEdt?.text.toString().trim()

        val username_pref=getSharedPreferences(ConstVal.MySharePref, Context.MODE_PRIVATE)
        val username= username_pref.getString(ConstVal.UserNameKeyPref,"username")
        val userid=FireStore().GetCurrentUserID()
        val product_obj= ProductDataClass( userid,username!!,title, Price.toInt(), desc,quantity.toInt(),downloadAble_Image_uri?:"")
        FireStore().addproductToFireStore(this, product_obj)

    }
    fun successAddproduct(){
        HideDialog()
        ShowSnackbar(resources.getString(R.string.success_addProduct),true)
    }
    fun failedAddproduct(){
        HideDialog()
        ShowSnackbar(resources.getString(R.string.failed_addProduct),false)

    }


}