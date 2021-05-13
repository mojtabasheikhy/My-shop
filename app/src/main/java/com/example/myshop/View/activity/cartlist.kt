package com.example.myshop.View.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.adapter.CartAdapter
import com.example.myshop.databinding.ActivityCartlistBinding
import com.example.myshop.model.CartDataClass
import com.google.firebase.database.core.view.View

class cartlist :Basic() {
    var cartListBinding:ActivityCartlistBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartListBinding = DataBindingUtil.setContentView(this,R.layout.activity_cartlist)
        actionbarSetup()

    }
    fun actionbarSetup() {
        setSupportActionBar(cartListBinding?.cartlistToolbar)
        val actionbar_history = supportActionBar
        if (actionbar_history != null) {
            actionbar_history.setDisplayHomeAsUpEnabled(true)
            actionbar_history.title = resources.getString(R.string.cartlist)
        }
        cartListBinding?.cartlistToolbar?.setNavigationIcon(R.drawable.ic_back)

        cartListBinding?.cartlistToolbar?.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        GetCartList()
    }
    fun GetCartList(){
        ShowDialog(resources.getString(R.string.wait))
        FireStore().GetCart(this)
    }
    fun GetCartListSuccess(cartList:ArrayList<CartDataClass>){
        HideDialog()
        if (cartList.size>0) {
            cartListBinding?.cartRecycler?.apply {
                var cartAdapter = CartAdapter()
                cartAdapter.setAllProductData(cartList)
                adapter = cartAdapter
                layoutManager = LinearLayoutManager(this@cartlist, RecyclerView.VERTICAL, false)
            }
        }
        else{
            cartListBinding?.checkoutLinearLayout?.visibility=android.view.View.GONE
            cartListBinding?.textView3?.visibility=android.view.View.VISIBLE
        }
    }
}