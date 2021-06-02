package com.example.myshop.View.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityAddAddressBinding
import com.example.myshop.model.AddressDataClass

class AddAddress : Basic(), View.OnClickListener {
    var addaddressbind: ActivityAddAddressBinding? = null
    var getExteraDetail: AddressDataClass? = null
    var activityResultContracts_location = registerForActivityResult(MapsActivity.resulatBack()) {
        val city = it.arrayList[1]
        val country = it.arrayList[2]
        val postalCode = it.arrayList[3]
        addaddressbind?.AddadsAddress?.setText(country+city)
        addaddressbind?.addadsZipcode?.setText(postalCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addaddressbind = DataBindingUtil.setContentView(this, R.layout.activity_add_address)
        setonclickListener()
        actionbarSetup()
    }

    fun setonclickListener() {
        addaddressbind?.AddadsSendBtn?.setOnClickListener(this)
        addaddressbind?.AddadsHome?.isChecked = true
        if (intent.hasExtra(ConstVal.addressDetailExtera)) {
            getExteraDetail = intent.getParcelableExtra(ConstVal.addressDetailExtera)
            setdata()
        }
        addaddressbind?.AddadsChoseType?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == R.id.Addads_other) {
                addaddressbind?.addadsLytOtherdetail?.visibility = View.VISIBLE
                addaddressbind?.addadsOtherdetail?.visibility = View.VISIBLE
            } else {
                addaddressbind?.addadsLytOtherdetail?.visibility = View.GONE
                addaddressbind?.addadsOtherdetail?.visibility = View.GONE

            }

        }
        addaddressbind?.AddadsLytAds?.setEndIconOnClickListener {
            CheckLocationPermisson()

        }

    }

    private fun CheckLocationPermisson() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            GiveLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                ConstVal.requestCode_AccessLocation
            )
        }
    }

    private fun GiveLocation() {
        activityResultContracts_location.launch(SuccessGettingLocation())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ConstVal.requestCode_AccessLocation) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GiveLocation()
            } else {
                ShowSnackbar(resources.getString(R.string.pleaseAllowPermision), false)
            }
        }
    }


    fun successUpdate() {
        Toast.makeText(
            this,
            resources.getString(R.string.SuccessWhenUpadteData),
            Toast.LENGTH_SHORT
        ).show()
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun failed() {
        Toast.makeText(this, resources.getString(R.string.errorWhenUpadteData), Toast.LENGTH_SHORT)
            .show()

    }

    private fun setdata() {
        if (getExteraDetail != null) {
            addaddressbind?.AddadressToolbar?.title = resources.getString(R.string.edit)

            addaddressbind?.addAdsFullname?.setText(getExteraDetail!!.fullname)
            addaddressbind?.AddadsPhonenumber?.setText(getExteraDetail!!.phonenumber.toString())
            addaddressbind?.AddadsAddress?.setText(getExteraDetail!!.address)
            addaddressbind?.addadsZipcode?.setText(getExteraDetail!!.zipcode.toString())
            addaddressbind?.addadsAdditional?.setText(getExteraDetail!!.additional_note)
            if (getExteraDetail!!.typeAddress.equals(ConstVal.Home)) {
                addaddressbind?.AddadsHome?.isChecked = true
            } else if (getExteraDetail!!.typeAddress.equals(ConstVal.office)) {
                addaddressbind?.AddadsOffice?.isChecked = true
            }
            if (getExteraDetail!!.typeAddress.equals(ConstVal.other)) {
                addaddressbind?.AddadsOther?.isChecked = true
                if (getExteraDetail!!.otherDetail.isNotEmpty()) {
                    addaddressbind?.AddadsOther?.isChecked = true
                    addaddressbind?.addadsOtherdetail?.setText(getExteraDetail!!.otherDetail)
                    addaddressbind?.addadsOtherdetail?.visibility = View.VISIBLE
                }
            }
        }
    }

    fun actionbarSetup() {
        setSupportActionBar(addaddressbind?.AddadressToolbar)
        val actionbar_history = supportActionBar
        if (actionbar_history != null) {
            actionbar_history.setDisplayHomeAsUpEnabled(true)
            actionbar_history.title = resources.getString(R.string.add_address)
        }
        addaddressbind?.AddadressToolbar?.setNavigationIcon(R.drawable.ic_back)
        addaddressbind?.AddadressToolbar?.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }

    fun validate_add_address(): Boolean {

        val fullname = addaddressbind?.addAdsFullname?.text.toString()
        val phonenumber = addaddressbind?.AddadsPhonenumber?.text.toString()
        val address = addaddressbind?.AddadsAddress?.text.toString()
        val zipcode = addaddressbind?.addadsZipcode?.text.toString()
        val additional = addaddressbind?.addadsAdditional?.text.toString()
        return when {
            TextUtils.isEmpty(fullname.trim({ it <= ' ' })) -> {
                ShowSnackbar(resources.getString(R.string.Enter_Name), false)
                false
            }
            TextUtils.isEmpty(phonenumber.trim({ it <= ' ' })) -> {
                ShowSnackbar(resources.getString(R.string.Enter_phone), false)
                false
            }
            TextUtils.isEmpty(address.trim({ it <= ' ' })) -> {
                ShowSnackbar(resources.getString(R.string.Enter_address), false)
                false
            }
            TextUtils.isEmpty(zipcode.trim({ it <= ' ' })) -> {
                ShowSnackbar(resources.getString(R.string.Enter_zipcode), false)
                false
            }
            TextUtils.isEmpty(additional.trim({ it <= ' ' })) -> {
                ShowSnackbar(resources.getString(R.string.Enter_additional), false)
                false
            }
            else -> {
                true
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Addads_send_btn -> {
                if (validate_add_address()) {
                    SendDataAddress()
                }
            }
            R.id.Addads_lyt_ads -> {

            }
        }
    }

    private fun SendDataAddress() {
        ShowDialog(resources.getString(R.string.wait))
        val fullname = addaddressbind?.addAdsFullname?.text.toString().trim()
        val phonenumber = addaddressbind?.AddadsPhonenumber?.text.toString().trim()
        val address = addaddressbind?.AddadsAddress?.text.toString().trim()
        val zipcode = addaddressbind?.addadsZipcode?.text.toString().trim()
        val additional = addaddressbind?.addadsAdditional?.text.toString().trim()
        val otherdetail = addaddressbind?.addadsOtherdetail?.text.toString().trim()

        val Address_Type = if (addaddressbind?.AddadsHome!!.isChecked) {
            ConstVal.Home
        } else if (addaddressbind?.AddadsOffice!!.isChecked) {
            ConstVal.office
        } else {
            ConstVal.other
        }
        var address_obj = AddressDataClass(
            fullname,
            FireStore().GetCurrentUserID(),
            phonenumber.toInt(),
            address,
            otherdetail,
            additional,
            zipcode.toInt(),
            Address_Type
        )
        if (getExteraDetail != null && getExteraDetail!!.address_id.isNotEmpty()) {
            FireStore().UpdateAddressDetail(this, address_obj, getExteraDetail!!.address_id)
        } else {
            FireStore().addAddressToFireStore(this, address_obj)
        }
    }

    fun successAddAddress() {
        Toast.makeText(this, resources.getString(R.string.successAdd), Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun failedAddAdress() {
        Toast.makeText(this, resources.getString(R.string.failedaddAddress), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        addaddressbind = null
    }

}
