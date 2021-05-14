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
import com.example.myshop.model.ProductDataClass
import com.google.firebase.database.core.view.View

class cartlist : Basic() {
    var cartListBinding: ActivityCartlistBinding? = null
    var MproductList: ArrayList<ProductDataClass>? = null
    var McartList: ArrayList<CartDataClass>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartListBinding = DataBindingUtil.setContentView(this, R.layout.activity_cartlist)
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

        GetproductList()
    }

    fun GetCartList() {
        ShowDialog(resources.getString(R.string.wait))
        FireStore().GetCart(this)
    }

    fun GetproductList() {
        //ShowDialog(resources.getString(R.string.wait))
        FireStore().GetallProductCartlist(this)
    }

    fun GetCartListSuccess(cartList: ArrayList<CartDataClass>) {
        HideDialog()
        for (i in MproductList!!) {
            for (y in cartList) {
                if (i.product_id == y.Productid) {
                    y.productQuantity = i.product_quantity
                    if (i.product_quantity == 0) {
                        y.card_quantity = i.product_quantity
                    }
                }
            }
        }
        McartList = cartList


        if (McartList!!.size > 0) {
            cartListBinding?.cartRecycler?.apply {
                var cartAdapter = CartAdapter(this@cartlist)
                cartAdapter.setAllProductData(cartList)
                adapter = cartAdapter
                layoutManager = LinearLayoutManager(this@cartlist, RecyclerView.VERTICAL, false)

            }
            var total: Int = 0
            for (item in McartList!!) {
                var avaiblequantity = item.productQuantity
                if (avaiblequantity > 0) {
                    var price: Int = item.price
                    var quantity = item.card_quantity
                    total += (price * quantity)
                }
            }
            cartListBinding?.cartSubtotalValue?.text = total.toString() + resources.getString(R.string.price_value)
            cartListBinding?.cartShipingValue?.text = "10000" + resources.getString(R.string.price_value)
            if (total > 0) {
                cartListBinding?.cartTotalamountValue?.text = (total + 10000).toString() + resources.getString(R.string.price_value)
            }
        } else {
            cartListBinding?.checkoutLinearLayout?.visibility = android.view.View.GONE
            cartListBinding?.textView3?.visibility = android.view.View.VISIBLE
            cartListBinding?.lottieAnimationView2?.visibility = android.view.View.VISIBLE
        }
    }

    fun SuccessGetAllProduct(productList: ArrayList<ProductDataClass>) {
        HideDialog()
        MproductList = productList
        GetCartList()
    }

    fun SuccessDeleteCartItem(){
        HideDialog()
        ShowSnackbar(resources.getString(R.string.deletedSuccessFullyCartitem),true)
        GetCartList()
    }

    fun SuccessUpdateCart(){
        HideDialog()
        GetCartList()
    }
}