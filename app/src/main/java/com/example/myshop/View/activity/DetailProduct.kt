package com.example.myshop.View.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityDetailProductBinding
import com.example.myshop.model.CartDataClass
import com.example.myshop.model.ProductDataClass
import java.lang.invoke.ConstantCallSite

class DetailProduct : Basic(), View.OnClickListener {
    lateinit var detailProductBinding: ActivityDetailProductBinding
    var userId: String? = null
    var productDetailGetsuccess: ProductDataClass? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailProductBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_detail_product)
        detailProductBinding.DetailAddtocart.setOnClickListener(this)
        GetDetailFromProduct()
        actionbarSetup()
    }

    fun actionbarSetup() {
        setSupportActionBar(detailProductBinding.detailToolbar)
        var actionbar_history = supportActionBar
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
            var productid = intent.getStringExtra(ConstVal.putExtera_detail_product)
            if (productid != null) {
                FireStore().GetDetailProduct(this, productid)
            }

        }
        if (intent.hasExtra(ConstVal.PutExtera_detail_userid)) {
            userId = intent.getStringExtra(ConstVal.PutExtera_detail_userid)
        }
        if (FireStore().GetCurrentUserID() == userId) {
            detailProductBinding.DetailAddtocart.visibility = View.GONE
        } else detailProductBinding.DetailAddtocart.visibility = View.VISIBLE
    }

    fun successGetDetailProduct(product: ProductDataClass) {
        HideDialog()
        productDetailGetsuccess = product




        ConstVal.LoadPicByGlide_noCircle(
            this,
            product.product_image,
            detailProductBinding.detailIvProduct
        )
        detailProductBinding.detailDescValue.text = product.product_desc
        detailProductBinding.detailPriceValue.text = product.product_pricce.toString() + "$"
        detailProductBinding.detailTitle.text = product.product_title
        detailProductBinding.detailQuantityValue.text = product.product_quantity.toString()
    }

    fun FailedGetDetailProduct() {
        HideDialog()
    }

    fun AddProductDetailToCart() {
        var CartObj = CartDataClass(
            FireStore().GetCurrentUserID(),
            productDetailGetsuccess!!.product_id,
            productDetailGetsuccess!!.product_title,
            productDetailGetsuccess!!.product_pricce.toString(),
            productDetailGetsuccess!!.product_image,
            productDetailGetsuccess!!.product_quantity.toString(),
            ConstVal.cart_quantity,

            )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Detail_addtocart -> {
                AddProductDetailToCart()
            }
        }
    }
}