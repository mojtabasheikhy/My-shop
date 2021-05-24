package com.example.myshop.View.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.adapter.AddressAdapter
import com.example.myshop.databinding.ActivityAddressBinding
import com.example.myshop.model.AddressDataClass
import com.myshoppal.utils.SwipeToDeleteCallback
import com.myshoppal.utils.SwipeToEditCallback
import java.sql.RowId

class Address : Basic(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    lateinit var addressBinding:ActivityAddressBinding
    var addressFromcheckout=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addressBinding=DataBindingUtil.setContentView(this,R.layout.activity_address)
        GetAllAddress()

        setSupportActionBar(addressBinding.AddressToolbar)
        val actionbar_history = supportActionBar
        actionbar_history?.setDisplayHomeAsUpEnabled(true)
        actionbarSetup()
        if (intent.hasExtra(ConstVal.pushExtera_selecet_address)){
            addressFromcheckout=intent.getBooleanExtra(ConstVal.pushExtera_selecet_address,false)

        }
        if (addressFromcheckout){
            if (actionbar_history != null) {
                actionbar_history.setTitle(resources.getString(R.string.selectAddreess))
            }
        }
        if (!addressFromcheckout){
            if (actionbar_history != null) {
                actionbar_history.setTitle(resources.getString(R.string.address))
            }
        }

        addressBinding.addressAddAds.setOnClickListener(this)
        addressBinding.addressSwip.setOnRefreshListener (this)


    }
    fun actionbarSetup() {
        addressBinding.AddressToolbar.setNavigationIcon(R.drawable.ic_back)
        addressBinding.AddressToolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }
    fun GetAllAddress(){
        showShimmer()
        FireStore().GetAllAdressOwn(this)
    }


    fun  successDelete(){
        HideDialog()
        ShowSnackbar(resources.getString(R.string.deletedSuccessFully),true)
        GetAllAddress()
    }
    fun failedDelete(){
        HideDialog()
        ShowSnackbar(resources.getString(R.string.deletedfailed),false)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.address_add_ads->{
                startActivityForResult(Intent(this,AddAddress::class.java),ConstVal.ActivityStartCode_selectAddress)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==ConstVal.ActivityStartCode_selectAddress){
            if (resultCode== Activity.RESULT_OK){
                GetAllAddress()
            }
        }
    }

    fun successGetMyAddressFromFireStore(address:ArrayList<AddressDataClass>){
        hideShimmer()
        addressBinding.addressSwip.isRefreshing=false
        if (address.size==0){
            addressBinding.noadsTxt.visibility=View.VISIBLE
            addressBinding.noaddsLottie.visibility=View.VISIBLE
            addressBinding.addressRecycler.visibility=View.GONE
        }
        else{
            addressBinding.noadsTxt.visibility=View.GONE
            addressBinding.noaddsLottie.visibility=View.GONE
            addressBinding.addressRecycler.apply {
                var adapter_address=AddressAdapter(this@Address,addressFromcheckout)
                adapter_address.setAllProductData(address)
                adapter=adapter_address
                layoutManager=LinearLayoutManager(this@Address,RecyclerView.VERTICAL,false)
            }

            if (!addressFromcheckout){
                val editorHandler=object :SwipeToEditCallback (this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter=addressBinding.addressRecycler.adapter as AddressAdapter
                        adapter.NotifyEdit(this@Address,viewHolder.adapterPosition)

                    }

                }
                val deleteAddressHandler=object :SwipeToDeleteCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        ShowDialog(resources.getString(R.string.wait))
                        FireStore().deleteAdress(this@Address,address[viewHolder.adapterPosition].address_id)
                    }

                }
                val editItemTouchHelper=ItemTouchHelper(editorHandler)
                val DeleteItemTouchHelper=ItemTouchHelper(deleteAddressHandler)
                DeleteItemTouchHelper.attachToRecyclerView(addressBinding.addressRecycler)
                editItemTouchHelper.attachToRecyclerView(addressBinding.addressRecycler)
            }

        }
    }
    fun hideShimmer(){
        addressBinding.addressRecycler.hideShimmer()
    }
    fun showShimmer(){
        addressBinding.addressRecycler.showShimmer()
    }

    override fun onRefresh() {
        addressBinding.noaddsLottie.visibility=View.GONE
        addressBinding.noadsTxt.visibility=View.GONE
        addressBinding.addressRecycler.visibility=View.VISIBLE
        GetAllAddress()
    }
}
