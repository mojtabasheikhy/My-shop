package com.example.myshop.View.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityDetailProductBinding
import com.example.myshop.databinding.ActivityForgotPasswordBindingImpl
import com.example.myshop.model.CartDataClass
import com.example.myshop.model.ProductDataClass
import com.google.android.material.snackbar.Snackbar
import java.lang.invoke.ConstantCallSite

class DetailProduct : Basic(), View.OnClickListener {
    lateinit var detailProductBinding: ActivityDetailProductBinding
    var userId: String? = null
    var product_id:String? = null
    var productDetailGetsuccess: ProductDataClass? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailProductBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_product)
        detailProductBinding.DetailAddtocart.setOnClickListener(this)
        detailProductBinding.DetailGotocart.setOnClickListener(this)
        GetDetailFromProduct()
        actionbarSetup()
    }

    fun actionbarSetup() {
        setSupportActionBar(detailProductBinding.detailToolbar)
        val actionbar_history = supportActionBar
        if (actionbar_history != null) {
            actionbar_history.setDisplayHomeAsUpEnabled(true)
            actionbar_history.title = resources.getString(R.string.detail)
        }
        detailProductBinding.detailToolbar.setNavigationIcon(R.drawable.ic_back)

        detailProductBinding.detailToolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }

    fun GetDetailFromProduct() {
        ShowDialog(resources.getString(R.string.wait))
        if (intent.hasExtra(ConstVal.putExtera_detail_product)) {
            product_id= intent.getStringExtra(ConstVal.putExtera_detail_product)
        }
        if (intent.hasExtra(ConstVal.PutExtera_detail_userid)) {
            userId = intent.getStringExtra(ConstVal.PutExtera_detail_userid)

        }
        if (FireStore().GetCurrentUserID() == userId) {
            detailProductBinding.DetailAddtocart.setText(resources.getString(R.string.ownproduct))
            detailProductBinding.DetailAddtocart.isEnabled=false
        }
        else{
            detailProductBinding.DetailAddtocart.visibility = View.VISIBLE

        }
        if (product_id != null) {
            FireStore().GetDetailProduct(this, product_id!!)

        }
    }

    @SuppressLint("SetTextI18n")
    fun successGetDetailProduct(product: ProductDataClass) {

        productDetailGetsuccess = product
        ConstVal.LoadPicByGlide_noCircle(this, product.product_image, detailProductBinding.detailIvProduct)
        detailProductBinding.detailDescValue.text = product.product_desc
        detailProductBinding.detailPriceValue.setText( product.product_pricce.toString() + resources.getString(R.string.price_value))
        detailProductBinding.detailTitle.text = product.product_title
        detailProductBinding.detailQuantityValue.text = product.product_quantity.toString()


        if (product.product_quantity== 0){
            HideDialog()
            ShowSnackbar(resources.getString(R.string.outofstockmessage),false)
            detailProductBinding.detailQuantityValue.text=resources.getString(R.string.outofstock)
            detailProductBinding.DetailAddtocart.isEnabled=false
            detailProductBinding.detailQuantityValue.setTextColor(ContextCompat.getColor(this,R.color.red))
        }
        else{
            FireStore().CheckProductExistInCart(this,product_id!!)
        }

        }


    fun FailedGetDetailProduct() {
        HideDialog()
    }

    fun AddProductDetailToCart() {
        val CartObj = CartDataClass(
            FireStore().GetCurrentUserID(),
            product_id!!,
            productDetailGetsuccess!!.product_title,
            productDetailGetsuccess!!.product_pricce,
            productDetailGetsuccess!!.product_image,
            productDetailGetsuccess!!.product_quantity,
            ConstVal.cart_quantity,
            )
        ShowDialog(resources.getString(R.string.wait))
        FireStore().CreateCartItem(this,CartObj)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Detail_addtocart -> {
                AddProductDetailToCart()
            }
            R.id.Detail_gotocart ->{
                val intent=Intent(this,cartlist::class.java)
                startActivity(intent)
            }
        }
    }
    fun AddCartSuccess(){
        HideDialog()
        detailProductBinding.DetailAddtocart.setText(R.string.cartitem)
        detailProductBinding.DetailAddtocart.isEnabled = false
        ShowSnackbar(resources.getString(R.string.successaddtocart),true)
    }

    fun failed() {
        HideDialog()
        Toast.makeText(this,"notadd",Toast.LENGTH_SHORT).show()
    }
    fun successExistInCart(){
        HideDialog()
        detailProductBinding.DetailAddtocart.setText(resources.getString(R.string.cartitem))
        detailProductBinding.DetailAddtocart.isEnabled = false
    }


}