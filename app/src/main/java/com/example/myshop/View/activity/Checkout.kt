package com.example.myshop.View.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.adapter.CartAdapter
import com.example.myshop.databinding.ActivityCheckoutBinding
import com.example.myshop.model.AddressDataClass
import com.example.myshop.model.CartDataClass
import com.example.myshop.model.OrderDataClass
import com.example.myshop.model.ProductDataClass

class Checkout : Basic(), android.view.View.OnClickListener {
    lateinit var checkoutBinding: ActivityCheckoutBinding
    var Mproductlist: ArrayList<ProductDataClass>? = null
    var MCartlist: ArrayList<CartDataClass>? = null
    var detailAddress: AddressDataClass? = null
    var total: Int = 0
    var totalamount:String=""
    var order_obj:OrderDataClass? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout)
        checkoutBinding.checkoutOrderplace.setOnClickListener(this)
        showShimer()
        actionbar_setup()
        GetAddressdata()
        getproduct()

    }

    private fun GetAddressdata() {
        if (intent.hasExtra(ConstVal.putExteraDetailAddressTocheckout)) {
            detailAddress =
                intent.getParcelableExtra<AddressDataClass>(ConstVal.putExteraDetailAddressTocheckout)
            if (detailAddress != null) {
                checkoutBinding.checkoutAddressTvFullnameValue.setText(detailAddress!!.fullname)
                checkoutBinding.checkoutTvAddressValue.setText(detailAddress!!.address)
                checkoutBinding.checkoutTvZipcodeValue.setText(detailAddress!!.zipcode.toString())
                checkoutBinding.checkoutAddressTypeValue.setText(detailAddress!!.typeAddress)
                checkoutBinding.checkoutTvAdinationalnoteValue.setText(detailAddress!!.additional_note)
                if (detailAddress!!.typeAddress == ConstVal.other) {
                    checkoutBinding.checkoutTvOtherDetailValue.visibility =
                        android.view.View.VISIBLE
                    checkoutBinding.checkoutTvOtherDetailValue.setText(detailAddress!!.otherDetail)
                } else {
                    checkoutBinding.checkoutTvOtherDetailValue.visibility = android.view.View.GONE
                }
            }
        }
    }

    fun actionbar_setup() {
        setSupportActionBar(checkoutBinding.materialToolbar)
        val actionbar_addproduct = supportActionBar
        if (actionbar_addproduct != null) {
            actionbar_addproduct.setDisplayHomeAsUpEnabled(true)
            actionbar_addproduct.title = resources.getString(R.string.checkout)
        }
        checkoutBinding.materialToolbar.setNavigationIcon(R.drawable.ic_back)
        checkoutBinding.materialToolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }

    fun successGetallProduct(ProductList: ArrayList<ProductDataClass>) {
        GetCartitem()
        Mproductlist = ProductList
    }

    fun getproduct() {
        FireStore().GetallProductCartlist(this)
    }

    fun GetCartitem() {
        FireStore().GetCart(this)
    }

    fun GetCartListSuccessCheckout(cartItem: ArrayList<CartDataClass>) {
        MCartlist = cartItem
        for (i in Mproductlist!!) {
            for (y in MCartlist!!) {
                if (i.product_id == y.User_id_Seller) {
                    y.product_Quantity = i.product_quantity
                }
            }
        }

        val adapter_obj = CartAdapter(this, false)
        adapter_obj.setAllProductData(MCartlist!!)
        checkoutBinding.shimmerRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@Checkout, RecyclerView.VERTICAL, false)
            adapter = adapter_obj
        }
        hideshimer()

        total = 0
        for (item in MCartlist!!) {
            var avaiblequantity = item.product_Quantity
            if (avaiblequantity > 0) {
                var price: Int = item.price
                var quantity = item.card_quantity
                total += (price * quantity)
            }
        }
        checkoutBinding.checkoutSubtotal.text = total.toString() + resources.getString(R.string.price_value)
        checkoutBinding.checkoutShipingCharge.text = "10000" + resources.getString(R.string.price_value)
        if (total > 0) {

            totalamount=(total + 10000).toString() + resources.getString(R.string.price_value)
            checkoutBinding.checkoutTotalamount.text =totalamount
            checkoutBinding.checkoutOrderplace.isEnabled = true

        } else {
            checkoutBinding.checkoutOrderplace.isEnabled = false
            checkoutBinding.checkoutOrderplace.setText(resources.getString(R.string.outofstock))
            checkoutBinding.checkoutOrderplace.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.second_text_color
                )
            )
        }


    }
    fun failedUpdateDetailProductAfterOrder(){
        Toast.makeText(this,resources.getString(R.string.errorWhenUpadteData),Toast.LENGTH_SHORT).show()
    }
    fun successUpdateDetailProductAfterOrder(){
        Toast.makeText(this,resources.getString(R.string.successAddingOrder),Toast.LENGTH_SHORT).show()
        var intent=Intent(this,Main::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun placeAnOrder() {
        if (totalamount != "") {
            ShowDialog(resources.getString(R.string.wait))
         order_obj = MCartlist?.let {
                OrderDataClass(
                    FireStore().GetCurrentUserID(),
                    it,
                    detailAddress!!,
                    "My order ${System.currentTimeMillis()}",
                    totalamount,
                    "10000",
                    total.toString(),
                    MCartlist!![0].Image_product,
                    System.currentTimeMillis()
                )
            }
            if (order_obj != null) {

                FireStore().CreateOrderCollection(this, order_obj!!)
            }
        }
        else {
             ShowSnackbar(resources.getString(R.string.yourproductoutofstock),false)
        }

    }

    fun hideshimer() {
        checkoutBinding.shimmerRecyclerView.hideShimmer()
    }

    fun showShimer() {
        checkoutBinding.shimmerRecyclerView.showShimmer()
    }
    fun successAddOrder(){
            FireStore().updateProductDetaiAfterOrder(this,MCartlist!!,order_obj!!)
    }
    fun FailaddOrder(){
        ShowSnackbar(resources.getString(R.string.failAddingOrder),true)
    }

    override fun onClick(v: android.view.View?) {
        when(v?.id){
            R.id.checkout_orderplace ->{
               placeAnOrder()
            }
        }
    }

}