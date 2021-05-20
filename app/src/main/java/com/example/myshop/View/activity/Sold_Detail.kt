package com.example.myshop.View.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.adapter.CartAdapter
import com.example.myshop.databinding.ActivitySoldDetailBinding
import com.example.myshop.model.OrderDataClass
import com.example.myshop.model.SoldDataClass
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Sold_Detail : AppCompatActivity() {
    lateinit var Sold_Detail_binding:ActivitySoldDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sold_Detail_binding=DataBindingUtil.setContentView(this,R.layout.activity_sold__detail)
        actionbarSetup()
        getDataExtera()
    }
    fun actionbarSetup() {
        setSupportActionBar(Sold_Detail_binding.soldDetailToolbar)
        val actionbar_history = supportActionBar
        if (actionbar_history != null) {
            actionbar_history.setDisplayHomeAsUpEnabled(true)
            actionbar_history.title = resources.getString(R.string.detail)
        }
        Sold_Detail_binding.soldDetailToolbar.setNavigationIcon(R.drawable.ic_back)

        Sold_Detail_binding.soldDetailToolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }

    fun getDataExtera(){
        if(intent.hasExtra(ConstVal.putExteraSolddetail)){
            var  detailSold=intent.getParcelableExtra<SoldDataClass>(ConstVal.putExteraSolddetail)
                    if (detailSold!=null){
                        Sold_Detail_binding.soldAmountValue.setText(detailSold.totalAmount)
                        Sold_Detail_binding.soldSubtotal.setText(detailSold.subtotal_amount)
                        Sold_Detail_binding.soldShipingValue.setText(detailSold.shiping_charge)

                       Sold_Detail_binding.soldFullname.setText(detailSold.Address_user_buyed.fullname)
                       Sold_Detail_binding.soldAddress.setText(detailSold.Address_user_buyed.address)
                       Sold_Detail_binding.soldZipcode.setText(detailSold.Address_user_buyed.zipcode.toString())
                        Sold_Detail_binding.soldAdditionalNote.setText(detailSold.Address_user_buyed.additional_note)
                        Sold_Detail_binding.soldMobilenumber.setText(detailSold.Address_user_buyed.phonenumber.toString())
                        val date="dd MMM yyyy HH:mm"
                        val formater= SimpleDateFormat(date, Locale.getDefault())
                        val calendar= Calendar.getInstance()
                        calendar.timeInMillis =detailSold.order_date
                        val orderTime=formater.format(calendar.time).toString()
                        Sold_Detail_binding.soldDate.text=orderTime
                        Sold_Detail_binding.soldQuantity.setText(detailSold.sold_quantity)
                        Sold_Detail_binding.soldPrice.setText(detailSold.Price)
                        ConstVal.LoadPicByGlide_noCircle(this,detailSold.image_product,Sold_Detail_binding.soldDetailImage)

                    }
                }
            }




}