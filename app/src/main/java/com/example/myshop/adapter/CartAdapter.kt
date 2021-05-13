package com.example.myshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.Utils.product_diff_util
import com.example.myshop.databinding.CartitemBinding
import com.example.myshop.databinding.ItemDashbordBinding
import com.example.myshop.model.CartDataClass
import com.example.myshop.model.ProductDataClass

class CartAdapter:RecyclerView.Adapter<CartAdapter.cartViewholder>() {
    var cartlistOld= emptyList<CartDataClass>()
    inner class cartViewholder(var cartitemBinding: CartitemBinding):RecyclerView.ViewHolder(cartitemBinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewholder {
        var view= CartitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return cartViewholder(view)
     }

    override fun getItemCount(): Int =cartlistOld.size

    override fun onBindViewHolder(holder: cartViewholder, position: Int) {
             holder.cartitemBinding.cartitemdata=cartlistOld[position]
     }
    fun setAllProductData(newcartlist:ArrayList<CartDataClass>){
        var difutils_obj = product_diff_util(newcartlist, cartlistOld)
        var difutils_calucate = DiffUtil.calculateDiff(difutils_obj)
        cartlistOld =  newcartlist
        difutils_calucate.dispatchUpdatesTo(this)
    }
}