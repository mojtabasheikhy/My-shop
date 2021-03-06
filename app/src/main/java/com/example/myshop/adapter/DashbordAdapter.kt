package com.example.myshop.adapter

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.Utils.product_diff_util
import com.example.myshop.databinding.ItemDashbordBinding
import com.example.myshop.model.ProductDataClass

class DashbordAdapter : RecyclerView.Adapter<DashbordAdapter.dashbordViewholder>() {
    var AllProduct = emptyList<ProductDataClass>()
    var clickListener: click? = null

    inner class dashbordViewholder(var dashbordbinding: ItemDashbordBinding) :
        RecyclerView.ViewHolder(dashbordbinding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dashbordViewholder {
        var view = ItemDashbordBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return dashbordViewholder(view)
    }

    override fun getItemCount(): Int = AllProduct.size
    override fun onBindViewHolder(holder: dashbordViewholder, position: Int) {
        if (position % 2 == 0) {
            val objectAnimator =
                ObjectAnimator.ofFloat(holder.itemView, android.view.View.TRANSLATION_X, -100f, 0f)
            objectAnimator.apply {
                duration = 1000
                repeatMode = ValueAnimator.REVERSE
                repeatCount = 0
                start()
            }
        } else {
            val objectAnimator =
                ObjectAnimator.ofFloat(holder.itemView, android.view.View.TRANSLATION_X, 100f, 0f)
            objectAnimator.apply {
                duration = 1000
                repeatMode = ValueAnimator.REVERSE
                repeatCount = 0
                start()
            }
        }
        holder.dashbordbinding.dashbord = AllProduct[position]
        holder.dashbordbinding.textView.setText(AllProduct[position].product_price.toString())
        holder.itemView.setOnClickListener {
            clickListener?.onclickListener(AllProduct[position], position)
        }
    }

    fun setAllProductData(newAllProduct: ArrayList<ProductDataClass>) {
        var difutils_obj = product_diff_util(newAllProduct, AllProduct)
        var difutils_calucate = DiffUtil.calculateDiff(difutils_obj)
        AllProduct = newAllProduct
        difutils_calucate.dispatchUpdatesTo(this)
    }

    fun setOnclickListener(myclick: click) {
        this.clickListener = myclick
    }

    interface click {
        fun onclickListener(productDataClass: ProductDataClass, position: Int)
    }
}