package com.example.myshop.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.Utils.ConstVal
import com.example.myshop.model.ProductDataClass
import com.example.myshop.Utils.product_diff_util
import com.example.myshop.View.activity.DetailProduct
import com.example.myshop.View.fragment.ProductFragment
import com.example.myshop.databinding.ItemProductBinding

class Product_adapter(var context: android.content.Context,var fragment:ProductFragment) : RecyclerView.Adapter<Product_adapter.ProductViewholder>() {
    var ProductList = emptyList<ProductDataClass>()

    inner class ProductViewholder(var ProductItem: ItemProductBinding) : RecyclerView.ViewHolder(ProductItem.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewholder {
        var Productview = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ProductViewholder(Productview)
    }

    override fun getItemCount(): Int = ProductList.size

    override fun onBindViewHolder(holder: ProductViewholder, position: Int) {
        holder.ProductItem.product = ProductList[position]
        holder.ProductItem.productDelete.setOnClickListener{
            //TODO show alertDialog
            fragment.deleteItem(ProductList[position].product_id)
        }
        holder.ProductItem.productMatrialroot.setOnClickListener {
             var intent_detail=Intent(context,DetailProduct::class.java)
             intent_detail.putExtra(ConstVal.putExtera_detail_product,ProductList[position].product_id)
             intent_detail.putExtra(ConstVal.PutExtera_detail_userid,ProductList[position].id)
             context.startActivity(intent_detail)
            
        }
    }

    fun setdata(newdata: ArrayList<ProductDataClass>) {
        var difutils_obj = product_diff_util(newdata, ProductList)
        var difutils_calucate = DiffUtil.calculateDiff(difutils_obj)
        ProductList = newdata
        difutils_calucate.dispatchUpdatesTo(this)

    }

}