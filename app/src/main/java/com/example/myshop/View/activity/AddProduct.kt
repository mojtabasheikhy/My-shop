package com.example.myshop.View.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityAddproductBinding
import com.example.myshop.model.ProductDataClass

class AddProduct : Basic(), View.OnClickListener{
    var addproductBinding: ActivityAddproductBinding? = null
    var imageuri_addproduct: Uri? = null
    var videouri_addproduct:Uri?=null
    var downloadAble_Image_uri:String?=null
    var downloadAble_video_uri:String?=null
    var newRequestPermissionChosepic =  registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it) {
            chooseImageOrvideo()
        }
    }
    var flag_chose_vide_or_pic:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addproductBinding = DataBindingUtil.setContentView(this, R.layout.activity_addproduct)
        actionbar_setup()
        OnClickListener()
    }

    private fun OnClickListener() {
        addproductBinding?.AddproductBtnSend?.setOnClickListener(this)
        addproductBinding?.addproductIvAddpic?.setOnClickListener(this)
        addproductBinding?.addVideoBtn?.setOnClickListener(this)
        addproductBinding?.changeVideBtn?.setOnClickListener(this)
        addproductBinding?.addProductChangeToPic?.setOnClickListener(this)
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
                flag_chose_vide_or_pic=false
                checkpermission()
            }
            R.id.addVideo_btn->{
                flag_chose_vide_or_pic=true
                checkpermission()
            }
            R.id.changeVide_btn ->{
                flag_chose_vide_or_pic=true
                checkpermission()
            }
            R.id.add_product_change_to_pic ->{
                flag_chose_vide_or_pic=false
                checkpermission()
            }
        }
    }

    private fun SendDataAddProductToFireStore() {
        ShowDialog(resources.getString(R.string.WaitToUpadteAndSendPicProduct))
        Toast.makeText(this,videouri_addproduct.toString()+imageuri_addproduct.toString(),Toast.LENGTH_SHORT).show()
        if (videouri_addproduct!=null && imageuri_addproduct==null){
            Toast.makeText(this,"videoo",Toast.LENGTH_SHORT).show()
            FireStore().UploadvideoToCloudStore(this, videouri_addproduct!!,ConstVal.AddProductVideo)
            Toast.makeText(this,videouri_addproduct.toString(),Toast.LENGTH_SHORT).show()
        }
        if (imageuri_addproduct!=null && videouri_addproduct==null){
            Toast.makeText(this,"image",Toast.LENGTH_SHORT).show()
            FireStore().UploadImageToCloudStore(this, imageuri_addproduct!!,ConstVal.AddProductImage)

        }
        if (videouri_addproduct==null && imageuri_addproduct==null){
            Toast.makeText(this,"not",Toast.LENGTH_SHORT).show()
            SendProductDetial()
        }

    }

    fun checkpermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            chooseImageOrvideo()
        } else {
            newRequestPermissionChosepic.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    fun chooseImageOrvideo() {
        if (flag_chose_vide_or_pic) {
            var videointent = Intent()
            videointent.type = "video/*"
            videointent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(videointent, ConstVal.RequestCode_Pick_Video_from_Gallery)

        } else {
            val GalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(GalleryIntent, ConstVal.RequestCode_Gallery)
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
       if(it!=null) {
           downloadAble_Image_uri = it.toString()
       }
        SendProductDetial()

    }

    private fun SendProductDetial() {
        val title = addproductBinding?.AddProductTitleEdt?.text.toString().trim()
        val Price = addproductBinding?.AddproductPriceEdt?.text.toString().trim()
        val desc =addproductBinding?.AddproductDescEdt?.text.toString().trim()
        val quantity =addproductBinding?.AddproductQuantityEdt?.text.toString().trim()

        val username_pref=getSharedPreferences(ConstVal.MySharePref, Context.MODE_PRIVATE)
        val userimge=username_pref.getString(ConstVal.userImageuri,"")
        val username= username_pref.getString(ConstVal.UserNameKeyPref,"username")

        val product_obj= username?.let {
            ProductDataClass( FireStore().GetCurrentUserID(),userimge ?:"", it,title, Price.toInt(), desc,quantity.toInt(),downloadAble_Image_uri?:"","",downloadAble_video_uri?:"")
        }
        if (product_obj != null) {
            FireStore().addproductToFireStore(this, product_obj)
        }

    }

    fun successAddproduct(){
        HideDialog()
        ShowSnackbar(resources.getString(R.string.success_addProduct),true)
        onBackPressed()
    }

    fun failedAddproduct(){
        HideDialog()
        ShowSnackbar(resources.getString(R.string.failed_addProduct),false)

    }

    override fun onDestroy() {
        super.onDestroy()
        addproductBinding=null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ConstVal.RequestCode_Permission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseImageOrvideo()
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

                        addproductBinding?.addproductIvAddpic?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit))
                        addproductBinding?.textView5?.visibility=View.GONE
                        addproductBinding?.addProductChangeToPic?.visibility=View.GONE
                        Toast.makeText(this, data.data.toString(), Toast.LENGTH_SHORT).show()
                        imageuri_addproduct = data.data
                        // Compelte_profile.CompleteIvUserprofile.setImageURI(Uri.parse(SelectedImage))
                        imageuri_addproduct?.let {

                            ConstVal.LoadPicByGlide_noCircle(this, it, addproductBinding!!.addproductIvShow)
                            ShowSnackbar(resources.getString(R.string.alerttouploadpic), true)
                        }
                        addproductBinding?.changeVideBtn?.visibility=View.GONE
                        addproductBinding?.addProductVideoView?.visibility=View.GONE
                        videouri_addproduct=null
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ShowSnackbar(resources.getString(R.string.faildSelectedImage), false)

                    }

                }
            }
            if (requestCode==ConstVal.RequestCode_Pick_Video_from_Gallery){
                if (data!=null) {
                    try {
                        addproductBinding?.addProductVideoView?.visibility = View.VISIBLE
                        Toast.makeText(this, data.data.toString(), Toast.LENGTH_SHORT).show()

                        addproductBinding?.addproductIvShow?.visibility = View.GONE
                        addproductBinding?.changeVideBtn?.visibility = View.VISIBLE
                        addproductBinding?.addProductChangeToPic?.visibility = View.VISIBLE
                        imageuri_addproduct = null
                        videouri_addproduct = data.data
                        setvideo_to_view(videouri_addproduct)
                    }
                    catch (e:Exception){
                        e.printStackTrace()
                        ShowSnackbar(resources.getString(R.string.faildSelectedImage), false)
                    }
                }

            }
        }
    }

    private fun setvideo_to_view(videouriAddproduct: Uri?) {

        addproductBinding?.addProductVideoView?.setVideoURI(videouriAddproduct)

    }

    fun UploadVideoSuccess(uri: Uri?) {
        if(uri!=null) {
            downloadAble_video_uri = uri.toString()

        }

        SendProductDetial()
    }


}