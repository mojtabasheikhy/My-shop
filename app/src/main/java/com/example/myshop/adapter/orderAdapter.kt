package com.example.myshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.Utils.ConstVal
import com.example.myshop.Utils.product_diff_util
import com.example.myshop.View.activity.DetailOrder
import com.example.myshop.databinding.ItemOrderBinding
import com.example.myshop.model.OrderDataClass

class orderAdapter(var context: Context): RecyclerView.Adapter<orderAdapter.orderViewholder>() {
    var Allorder= emptyList<OrderDataClass>()

    inner class orderViewholder(var orderBinding: ItemOrderBinding): RecyclerView.ViewHolder(orderBinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderViewholder {
        var view=ItemOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return orderViewholder(view)
    }
    override fun getItemCount(): Int =Allorder.size
    override fun onBindViewHolder(holder: orderViewholder, position: Int) {
        holder.orderBinding.order=Allorder[position]
        holder.orderBinding.orderRoot.setOnClickListener{
            var intent=Intent(context,DetailOrder::class.java)
            intent.putExtra(ConstVal.PutExtra_OrderDetail,Allorder[position])
            context.startActivity(intent)
        }

    }
    fun setAllOrderData(newOrderlist:List<OrderDataClass>){
        var difutils_obj = product_diff_util(newOrderlist, Allorder)
        var difutils_calucate = DiffUtil.calculateDiff(difutils_obj)
        Allorder =  newOrderlist
        difutils_calucate.dispatchUpdatesTo(this)
    }

}