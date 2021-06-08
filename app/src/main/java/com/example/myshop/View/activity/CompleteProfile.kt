package com.example.myshop.View.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityCompleteProfileBinding
import com.example.myshop.model.Users

class CompleteProfile : Basic(), View.OnClickListener {
    var Complete_profile_binding: ActivityCompleteProfileBinding? = null
    var actionbar_complete: androidx.appcompat.app.ActionBar? = null
    var ImageUriSelected: Uri? = null
    var downloadAble: String? = null
    var usersDetail: Users? = null
    var CheckPermision_for_chose_image =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                val GalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                GalleryIntent.setAction(Intent.ACTION_GET_CONTENT)
                resulat_back_when_chose_image.launch(GalleryIntent)
            } else {
                ShowSnackbar(resources.getString(R.string.pleaseAllowPermision), false)
            }

        }
    var resulat_back_when_chose_image = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data != null) {
                    try {
                        ImageUriSelected = it.data!!.data
                        // Complete_profile.CompleteIvUserprofile.setImageURI(Uri.parse(SelectedImage))
                        ImageUriSelected?.let {
                            Load_Pic_Profile(it)
                            ShowSnackbar(resources.getString(R.string.alerttouploadpic), true)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ShowSnackbar(resources.getString(R.string.faildSelectedImage), false)

                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Complete_profile_binding =
            DataBindingUtil.setContentView(this, R.layout.activity_complete_profile)
        setUpActionbar()
        GetUserDetailFromRegister()
        SetOnClickListener()

    }

    fun setUpActionbar() {
        setSupportActionBar(Complete_profile_binding?.compeleteToolbar)
        actionbar_complete?.setDisplayHomeAsUpEnabled(true)
        actionbar_complete = supportActionBar
    }

    private fun Disable_Edt_Which_HasData() {
        Complete_profile_binding?.CompleteEdtName?.isEnabled = false
        Complete_profile_binding?.completeEdtPhoneNumber?.isEnabled = false
        Complete_profile_binding?.completeEdtEmail?.isEnabled = false

    }

    fun SetOnClickListener() {
        Complete_profile_binding?.CompleteIvUserprofile?.setOnClickListener(this)
        Complete_profile_binding?.CompleteBtnSave?.setOnClickListener(this)
    }

    private fun GetUserDetailFromRegister() {
        val Intent_GetUser_Detail = intent
        if (Intent_GetUser_Detail.hasExtra(ConstVal.PutExtra_UserDetail)) {
            usersDetail =
                Intent_GetUser_Detail.getParcelableExtra<Users>(ConstVal.PutExtra_UserDetail)

            if (usersDetail != null) {
                if (usersDetail?.profile_Compelete == 0) {
                    Disable_Edt_Which_HasData()
                    Complete_profile_binding?.CompleteRdGenderFemale?.isChecked = true
                    actionbar_complete?.setTitle(resources.getString(R.string.Compeleteprofile))
                    if (usersDetail?.Image!!.isNotEmpty()) {
                        ConstVal.LoadPicByGlide(
                            this,
                            usersDetail!!.Image,
                            Complete_profile_binding!!.CompleteIvUserprofile
                        )
                    }

                } else {
                    Complete_profile_binding?.completeEdtEmail?.isEnabled = false
                    actionbar_complete?.setTitle(resources.getString(R.string.edit))
                    if (usersDetail?.Image != null) {
                        Load_Pic_Profile(usersDetail!!.Image)
                    }
                    Complete_profile_binding?.completeEdtLastName?.setText(usersDetail?.lastName)

                    val gender = usersDetail?.gender
                    if (gender == ConstVal.Male) {
                        Complete_profile_binding?.CompleteRdGenderMale?.isChecked = true
                    } else {
                        Complete_profile_binding?.CompleteRdGenderFemale?.isChecked = true
                    }
                }
            }

        }
        SetDataToEdtGiveBefore(usersDetail)
        Complete_profile_binding?.compeleteToolbar?.setNavigationIcon(R.drawable.ic_back)
        Complete_profile_binding?.compeleteToolbar?.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun SetDataToEdtGiveBefore(usersDetail: Users?) {
        Complete_profile_binding?.completeEdtEmail?.setText(usersDetail?.Email)
        Complete_profile_binding?.CompleteEdtName?.setText(usersDetail?.FirstName)
        if (usersDetail?.Mobile.equals("null")) {
            Complete_profile_binding?.completeEdtPhoneNumber?.setText("")
            Complete_profile_binding?.completeEdtPhoneNumber?.isEnabled = true
        } else {
            Complete_profile_binding?.completeEdtPhoneNumber?.setText(usersDetail?.Mobile.toString())
        }
        if (usersDetail?.profile_Compelete == 1) {
            Complete_profile_binding?.completeEdtLastName?.setText(usersDetail.lastName)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Complete_Iv_userprofile -> {
                CheckPermission()
            }
            R.id.Complete_Btn_save -> {

                if (ValidationLastName()) {
                    GiveData_To_completeSetup()
                }
            }

        }
    }

    private fun GiveData_To_completeSetup() {
        if (ImageUriSelected != null) {
            FireStore().UploadImageToCloudStore(this, ImageUriSelected!!, ConstVal.UserProfileImage)
            ShowDialog(resources.getString(R.string.WaitToUpadteAndSendPic))

        } else {
            ShowDialog(resources.getString(R.string.WaitToUpadte))
            UpdateUsersDetail()
        }
    }

    private fun UpdateUsersDetail() {
        val LastName = Complete_profile_binding?.completeEdtLastName?.text.toString()
        val firstname = Complete_profile_binding?.CompleteEdtName?.text.toString()
        val mobile = Complete_profile_binding?.completeEdtPhoneNumber?.text.toString()
        val UserHashMap = HashMap<String, Any>()



        if (firstname.isNotEmpty() && firstname != usersDetail?.FirstName) {
            UserHashMap[ConstVal.name] = firstname
        }
        if (mobile.isNotEmpty() && mobile != usersDetail?.Mobile.toString()) {
            UserHashMap[ConstVal.phonenumber] = mobile
        }
        if (LastName.isNotEmpty() && LastName != usersDetail?.lastName) {
            UserHashMap[ConstVal.Lastname] = LastName
        }
        if (downloadAble != null && !downloadAble.equals(usersDetail?.Image)) {
            UserHashMap[ConstVal.imageColumn] = downloadAble.toString()
        }

        val gender = if (Complete_profile_binding?.CompleteRdGenderFemale!!.isChecked) {
            ConstVal.Female
        } else {
            ConstVal.Male
        }


        if (gender.isNotEmpty() && gender != usersDetail?.gender) {
            UserHashMap[ConstVal.gender] = gender
        }

        UserHashMap[ConstVal.usercompelete] = 1
        FireStore().Update_User_Detail(this, UserHashMap)


    }

    private fun ValidationLastName(): Boolean {
        return when {
            TextUtils.isEmpty(
                Complete_profile_binding?.completeEdtLastName?.text.toString()
                    .trim { it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.Enter_lastname), false)
                false
            }
            else -> true
        }
    }

    private fun CheckPermission() {
        //this  ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        /*  if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
           ) {
               ConstVal.ChoseImageFromGallery(this@CompleteProfile)
           } else {
               ActivityCompat.requestPermissions(
                   this,
                   arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                   ConstVal.RequestCode_Permission
               )
           }*/
        CheckPermision_for_chose_image.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun Load_Pic_Profile(image: Any) {
        ConstVal.LoadPicByGlide(this, image, Complete_profile_binding!!.CompleteIvUserprofile)
    }

    fun uploadImageSuccess(download_able_uri_of_image: Uri?) {
        if (download_able_uri_of_image != null) {
            downloadAble = download_able_uri_of_image.toString()
            val pref = getSharedPreferences(ConstVal.MySharePref, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(ConstVal.userImageuri, download_able_uri_of_image.toString())
            editor.apply()
        }
        UpdateUsersDetail()

    }

    fun UpdateUserDetailSuccessfully() {
        if (!this@CompleteProfile.isFinishing) {
            HideDialog()
        }
        if (usersDetail != null) {
            if (usersDetail?.profile_Compelete == 0) {
                Custom_Toast(this, resources.getString(R.string.welcome) + usersDetail?.FirstName.toString(),R.drawable.ic_checkbox_icon,R.color.text_color)
                startActivity(Intent(this, Main::class.java))
                finish()
            }
            if (usersDetail?.profile_Compelete == 1) {
                Custom_Toast(this, resources.getString(R.string.SuccessWhenUpadteData),R.drawable.ic_checkbox_icon,R.color.text_color)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Complete_profile_binding = null
    }


}