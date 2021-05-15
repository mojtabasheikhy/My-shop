package com.example.myshop.View.activity

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
import com.example.myshop.adapter.AddressAdapter
import com.example.myshop.databinding.ActivityAddressBinding
import com.example.myshop.model.AddressDataClass
import com.myshoppal.utils.SwipeToDeleteCallback
import com.myshoppal.utils.SwipeToEditCallback
import java.sql.RowId

class Address : Basic(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    lateinit var addressBinding:ActivityAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addressBinding=DataBindingUtil.setContentView(this,R.layout.activity_address)
        addressBinding.addressAddAds.setOnClickListener(this)
        addressBinding.addressSwip.setOnRefreshListener (this)
        actionbarSetup()
        GetAllAddress()
    }
    fun actionbarSetup() {
        setSupportActionBar(addressBinding.AddressToolbar)
        val actionbar_history = supportActionBar
        if (actionbar_history != null) {
            actionbar_history.setDisplayHomeAsUpEnabled(true)
            actionbar_history.title = resources.getString(R.string.address)
        }
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

    override fun onResume() {
        GetAllAddress()
        super.onResume()

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
                startActivity(Intent(this,AddAddress::class.java))
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
                var adapter_address=AddressAdapter(this@Address)
                adapter_address.setAllProductData(address)
                adapter=adapter_address
                layoutManager=LinearLayoutManager(this@Address,RecyclerView.VERTICAL,false)
            }
            var editorHandler=object :SwipeToEditCallback (this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                         var adapter=addressBinding.addressRecycler.adapter as AddressAdapter
                         adapter.NotifyEdit(this@Address,viewHolder.adapterPosition)

                }

            }
            var deleteAddressHandler=object :SwipeToDeleteCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    ShowDialog(resources.getString(R.string.wait))
                    FireStore().deleteAdress(this@Address,address[viewHolder.adapterPosition].address_id)
                }

            }
            var editItemTouchHelper=ItemTouchHelper(editorHandler)
            var DeleteItemTouchHelper=ItemTouchHelper(deleteAddressHandler)
            DeleteItemTouchHelper.attachToRecyclerView(addressBinding.addressRecycler)
            editItemTouchHelper.attachToRecyclerView(addressBinding.addressRecycler)
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
