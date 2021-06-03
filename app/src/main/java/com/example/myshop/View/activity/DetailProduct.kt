package com.example.myshop.View.activity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityDetailProductBinding
import com.example.myshop.model.CartDataClass
import com.example.myshop.model.ProductDataClass

class DetailProduct : Basic(), View.OnClickListener {
    lateinit var detailProductBinding: ActivityDetailProductBinding
    var userId_seller: String? = null
    var product_id: String? = null
    var DownloadURL:String?=null
    var productDetailGetsuccess: ProductDataClass? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailProductBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_detail_product
        )

        detailProductBinding.DetailAddtocart.setOnClickListener(this)
        detailProductBinding.DetailGotocart.setOnClickListener(this)
        detailProductBinding.detailProductDownload.setOnClickListener(this)
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
            product_id = intent.getStringExtra(ConstVal.putExtera_detail_product)
        }
        if (intent.hasExtra(ConstVal.PutExtera_detail_userid)) {
            userId_seller = intent.getStringExtra(ConstVal.PutExtera_detail_userid)
        }
        if (FireStore().GetCurrentUserID() == userId_seller) {
            detailProductBinding.DetailAddtocart.setText(resources.getString(R.string.ownproduct))
            detailProductBinding.DetailAddtocart.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.second_text_color
                )
            )
            detailProductBinding.DetailAddtocart.isEnabled = false
        } else {
            detailProductBinding.DetailAddtocart.isEnabled = true
        }
        if (product_id != null) {
            FireStore().GetDetailProduct(this, product_id!!)

        }
    }

    @SuppressLint("SetTextI18n")
    fun successGetDetailProduct(product: ProductDataClass) {

        productDetailGetsuccess = product
        product.let {
            detailProductBinding.detailDescValue.text = product.product_desc
            detailProductBinding.detailPriceValue.setText(
                it.product_price.toString() + resources.getString(
                    R.string.price_value
                )
            )
            detailProductBinding.detailTitle.text = it.product_title
            detailProductBinding.detailQuantityValue.text = it.product_quantity.toString()
            if (productDetailGetsuccess!!.product_Video.isNotEmpty()) {
                productDetailGetsuccess!!.product_Video.let {
                    detailProductBinding.detailProductDownload.visibility=View.VISIBLE
                    detailProductBinding.detailIvProduct.visibility = View.GONE
                    detailProductBinding.detailProductVideoView.visibility = View.VISIBLE
                    DownloadURL=productDetailGetsuccess!!.product_Video
                    setvideo_to_view(it)
                }
            }
            if (product.product_image.isEmpty() && product.product_Video.isEmpty()) {
                detailProductBinding.detailNopicTv.visibility = View.VISIBLE
                detailProductBinding.detailIvProduct.visibility = View.VISIBLE
                detailProductBinding.detailProductDownload.visibility=View.GONE
                detailProductBinding.detailProductVideoView.visibility = View.GONE
                detailProductBinding.detailIvProduct.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_baseline_image_chose
                    )
                )
                detailProductBinding.detailIvProduct.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.second_text_color
                    )
                )
            }

            if (product.product_image.isNotEmpty() && product.product_Video.isEmpty()) {
                detailProductBinding.detailProductDownload.visibility=View.VISIBLE
                DownloadURL=productDetailGetsuccess!!.product_image
                ConstVal.LoadPicByGlide_noCircle(
                    this,
                    it.product_image,
                    detailProductBinding.detailIvProduct
                )
            }

            if (it.product_quantity == 0) {
                HideDialog()
                ShowSnackbar(resources.getString(R.string.outofstockmessage), false)
                detailProductBinding.detailQuantityValue.text =
                    resources.getString(R.string.outofstock)
                detailProductBinding.DetailAddtocart.isEnabled = false
                detailProductBinding.detailQuantityValue.setTextColor(
                    ContextCompat.getColor(this, R.color.red)
                )
            } else {
                FireStore().CheckProductExistInCart(this, product_id!!)
            }
        }

    }


    fun FailedGetDetailProduct() {
        HideDialog()
    }

    fun AddProductDetailToCart() {
        val CartObj = CartDataClass(
            FireStore().GetCurrentUserID(),
            userId_seller!!,
            product_id!!,
            productDetailGetsuccess!!.product_title,
            productDetailGetsuccess!!.product_price,
            productDetailGetsuccess!!.product_image,
            productDetailGetsuccess!!.product_quantity,
            ConstVal.cart_quantity,
        )
        ShowDialog(resources.getString(R.string.wait))
        FireStore().CreateCartItem(this, CartObj)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Detail_addtocart -> {
                AddProductDetailToCart()
            }
            R.id.Detail_gotocart -> {
                val intent = Intent(this, cartlist::class.java)
                startActivity(intent)
            }
            R.id.detail_Product_download -> {
                if (DownloadURL!!.isNotEmpty()) {
                    download_pic_video(DownloadURL!!)
                }
            }
        }
    }

    private fun download_pic_video(filepath:String){
         try {
             val downloadmanager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
             val uri = Uri.parse(filepath)
             val request = DownloadManager.Request(uri)

             request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

             downloadmanager.enqueue(request)
         }
         catch (e:Exception){
             Toast.makeText(this@DetailProduct, e.message.toString(),Toast.LENGTH_SHORT).show()
         }


    }

    fun AddCartSuccess() {
        HideDialog()
        detailProductBinding.DetailAddtocart.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.second_text_color
            )
        )
        detailProductBinding.DetailAddtocart.setText(R.string.cartitem)
        detailProductBinding.DetailAddtocart.isEnabled = false
        ShowSnackbar(resources.getString(R.string.successaddtocart), true)
    }

    fun AddCartfailed() {
        HideDialog()
        Toast.makeText(this, resources.getString(R.string.errorinaddtocart), Toast.LENGTH_SHORT)
            .show()
    }

    fun successExistInCart() {
        HideDialog()
        detailProductBinding.DetailAddtocart.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.second_text_color
            )
        )
        detailProductBinding.DetailAddtocart.setText(resources.getString(R.string.cartitem))
        detailProductBinding.DetailAddtocart.isEnabled = false
    }

    private fun setvideo_to_view(videouriAddproduct: String?) {
        detailProductBinding?.detailProductVideoView?.setVideoURI(Uri.parse(videouriAddproduct))
    }


}