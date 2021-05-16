package com.example.myshop.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.Utils.product_diff_util
import com.example.myshop.View.activity.AddAddress
import com.example.myshop.View.activity.Address
import com.example.myshop.View.activity.Checkout
import com.example.myshop.View.activity.cartlist
import com.example.myshop.databinding.CartitemBinding
import com.example.myshop.databinding.ItemAddressBinding
import com.example.myshop.databinding.ItemDashbordBinding
import com.example.myshop.model.AddressDataClass
import com.example.myshop.model.CartDataClass
import com.example.myshop.model.ProductDataClass

class AddressAdapter(
    var context: Context,
    var selectedAddress: Boolean
) : RecyclerView.Adapter<AddressAdapter.AddressViewholder>() {
    var AddresslistOld = emptyList<AddressDataClass>()

    inner class AddressViewholder(var addressBinding: ItemAddressBinding) :
        RecyclerView.ViewHolder(addressBinding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewholder {
        var view = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewholder(view)
    }

    override fun getItemCount(): Int = AddresslistOld.size

    override fun onBindViewHolder(holder: AddressViewholder, position: Int) {
        holder.addressBinding.address = AddresslistOld[position]
        if (selectedAddress) {
            holder.addressBinding.root.setOnClickListener {
                var intent=Intent(context,Checkout::class.java)
                intent.putExtra(ConstVal.putExteraDetailAddressTocheckout,AddresslistOld[position])
                context.startActivity(intent)
            }
        }
    }

    fun setAllProductData(newcartlist: ArrayList<AddressDataClass>) {
        var difutils_obj = product_diff_util(newcartlist, AddresslistOld)
        var difutils_calucate = DiffUtil.calculateDiff(difutils_obj)
        AddresslistOld = newcartlist
        difutils_calucate.dispatchUpdatesTo(this)
    }

    fun NotifyEdit(activity: Activity, position: Int) {
        var intent = Intent(context, AddAddress::class.java)
        intent.putExtra(ConstVal.addressDetailExtera, AddresslistOld[position])
        activity.startActivity(intent)
        notifyItemChanged(position)
    }


}