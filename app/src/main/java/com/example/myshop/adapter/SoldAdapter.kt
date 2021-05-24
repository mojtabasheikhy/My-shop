package com.example.myshop.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.Utils.ConstVal
import com.example.myshop.Utils.product_diff_util
import com.example.myshop.View.activity.Sold_Detail
import com.example.myshop.databinding.ItemSoldBinding
import com.example.myshop.model.SoldDataClass
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SoldAdapter(var context: android.content.Context) : RecyclerView.Adapter<SoldAdapter.SoldViewholder>() {
    var sold_List = emptyList<SoldDataClass>()

    inner class SoldViewholder(var sold_item: ItemSoldBinding) :
        RecyclerView.ViewHolder(sold_item.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldViewholder {
        val Productview =
            ItemSoldBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoldViewholder(Productview)
    }

    override fun getItemCount(): Int = sold_List.size

    override fun onBindViewHolder(holder: SoldViewholder, position: Int) {
        holder.sold_item.sold = sold_List[position]
        holder.sold_item.SoldRoot.setOnClickListener {
            var intent= Intent(context, Sold_Detail::class.java)
            intent.putExtra(ConstVal.putExteraSolddetail,sold_List[position])
            context.startActivity(intent)
        }
        val date="dd MMM yyyy HH:mm"
        val formater= SimpleDateFormat(date, Locale.getDefault())
        val calendar= Calendar.getInstance()
        calendar.timeInMillis =sold_List[position].order_date
        val orderTime=formater.format(calendar.time)
        holder.sold_item.soldTextView9.setText(orderTime)


    }

    fun setdataSold(newdata: ArrayList<SoldDataClass>) {
        val difutils_obj = product_diff_util(newdata, sold_List)
        val difutils_calucate = DiffUtil.calculateDiff(difutils_obj)
        sold_List = newdata
        difutils_calucate.dispatchUpdatesTo(this)
    }
}