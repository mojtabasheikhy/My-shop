package com.example.myshop.View.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.adapter.CartAdapter
import com.example.myshop.databinding.ActivityDetailOrderBinding
import com.example.myshop.model.OrderDataClass
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DetailOrder :Basic() {
    lateinit var DetailOrderBinding:ActivityDetailOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DetailOrderBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail_order)
        actionbar_setup()
        GetDetail()


    }
    fun actionbar_setup() {
        setSupportActionBar(DetailOrderBinding.detailOrderToolbar)
        val actionbar_detailorder = supportActionBar
        if (actionbar_detailorder != null) {
            actionbar_detailorder.setDisplayHomeAsUpEnabled(true)
            actionbar_detailorder.title = resources.getString(R.string.detailOrder)
        }
        DetailOrderBinding.detailOrderToolbar.setNavigationIcon(R.drawable.ic_back)
        DetailOrderBinding.detailOrderToolbar.setNavigationIconTint(ContextCompat.getColor(this,R.color.white

        ))
        DetailOrderBinding.detailOrderToolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }
    fun GetDetail(){
        if (intent.hasExtra(ConstVal.PutExtra_OrderDetail)){
            var GetDetail=intent.getParcelableExtra<OrderDataClass>(ConstVal.PutExtra_OrderDetail)
            if (GetDetail!=null){
                DetailOrderBinding.detailAmountValue.setText(GetDetail.totalAmount)
                DetailOrderBinding.detailOrderShipingValue.setText(GetDetail.shipingCharge)
                DetailOrderBinding.detailOrderSubtotal.setText(GetDetail.subtotal)

                DetailOrderBinding.detailOrderId.setText(GetDetail.order_id)
                DetailOrderBinding.detialOrderAddress.setText(GetDetail.address_buyer.address)
                DetailOrderBinding.detialOrderFullname.setText(GetDetail.address_buyer.fullname)
                DetailOrderBinding.detialOrderZipcode.setText(GetDetail.address_buyer.zipcode.toString())
                DetailOrderBinding.detialOrderMobilenumber.setText(GetDetail.address_buyer.phonenumber.toString())



                val date="dd MMM yyyy HH:mm"
                val formater=SimpleDateFormat(date, Locale.getDefault())
                val calendar=Calendar.getInstance()
                calendar.timeInMillis =GetDetail.orderDate
                val orderTime=formater.format(calendar.time)
                DetailOrderBinding.detailDate.setText(orderTime)


                val diffInMillsecond=System.currentTimeMillis() - GetDetail.orderDate
                val diffHours=TimeUnit.MILLISECONDS.toHours(diffInMillsecond)

                when{
                    diffHours < 1 ->{
                      DetailOrderBinding.detailOrderStatus.text = resources.getString(R.string.pending)
                        DetailOrderBinding.detailOrderStatus.setTextColor(ContextCompat.getColor(this,R.color.pink))
                    }

                    diffHours < 3 ->{
                        DetailOrderBinding.detailOrderStatus.text = resources.getString(R.string.process)
                        DetailOrderBinding.detailOrderStatus.setTextColor(ContextCompat.getColor(this,R.color.orange))
                    }
                    else ->{
                        DetailOrderBinding.detailOrderStatus.text = resources.getString(R.string.delivred)
                        DetailOrderBinding.detailOrderStatus.setTextColor(ContextCompat.getColor(this,R.color.green))
                    }
                }


                DetailOrderBinding.detailOrderListRecycler.apply {

                    layoutManager=LinearLayoutManager(this@DetailOrder,RecyclerView.VERTICAL,false)

                }
                val adapterDetail=CartAdapter(this,false)
                adapterDetail.setAllProductData(GetDetail.cartitem)
                DetailOrderBinding.detailOrderListRecycler.adapter=adapterDetail



            }
        }
    }
}