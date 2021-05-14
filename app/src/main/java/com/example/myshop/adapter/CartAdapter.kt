package com.example.myshop.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.Utils.product_diff_util
import com.example.myshop.View.activity.cartlist
import com.example.myshop.databinding.CartitemBinding
import com.example.myshop.databinding.ItemDashbordBinding
import com.example.myshop.model.CartDataClass
import com.example.myshop.model.ProductDataClass

class CartAdapter(var context: Context) : RecyclerView.Adapter<CartAdapter.cartViewholder>() {
    var cartlistOld = emptyList<CartDataClass>()

    inner class cartViewholder(var cartitemBinding: CartitemBinding) :
        RecyclerView.ViewHolder(cartitemBinding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewholder {
        var view = CartitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cartViewholder(view)
    }

    override fun getItemCount(): Int = cartlistOld.size

    override fun onBindViewHolder(holder: cartViewholder, position: Int) {
        holder.cartitemBinding.cartitemdata = cartlistOld[position]
        holder.cartitemBinding.cartItemQuantityValue.text = cartlistOld[position].card_quantity.toString()


        if (cartlistOld[position].productQuantity == 0) {
            holder.cartitemBinding.cartItemAddproduct.visibility = View.GONE
            holder.cartitemBinding.cartItemRemoveproduct.visibility = View.GONE
            holder.cartitemBinding.cartItemQuantityValue.setText(context.resources.getString(R.string.outofstock))
            holder.cartitemBinding.cartItemQuantityValue.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.red
                )
            )
            holder.cartitemBinding.cartIv.alpha = 0.5f
            holder.cartitemBinding.cartItemPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
        } else {
            holder.cartitemBinding.cartItemDelete.visibility = View.VISIBLE
            holder.cartitemBinding.cartItemAddproduct.visibility = View.VISIBLE
            holder.cartitemBinding.cartItemQuantityValue.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.text_color
                )
            )
        }
        holder.cartitemBinding.cartItemDelete.setOnClickListener {
            when (context) {
                is cartlist -> {
                    (context as cartlist).ShowDialog(context.resources.getString(R.string.wait))
                }
            }

            FireStore().DeleteCartItem(context, cartlistOld[position].cart_id)
        }
        holder.cartitemBinding.cartItemRemoveproduct.setOnClickListener {
            if (cartlistOld[position].card_quantity == 1) {
                FireStore().DeleteCartItem(context, cartlistOld[position].cart_id)
            } else {
                val cartquantity: Int = cartlistOld[position].card_quantity
                val itemHashMap = HashMap<String, Any>()
                itemHashMap[ConstVal.cart_quantity_colmn] = cartquantity - 1
                if (context is cartlist) {
                    (context as cartlist).ShowDialog(context.resources.getString(R.string.wait))
                    FireStore().UpdateDetailCart(
                        cartlistOld[position].cart_id,
                        context,
                        itemHashMap
                    )
                }
            }
        }
        holder.cartitemBinding.cartItemAddproduct.setOnClickListener {
            var cartquantity: Int = cartlistOld[position].card_quantity
            if (cartlistOld[position].card_quantity < cartlistOld[position].productQuantity) {
                var itemHashMap = HashMap<String, Any>()
                itemHashMap[ConstVal.cart_quantity_colmn] = cartquantity + 1
                if (context is cartlist) {
                    (context as cartlist).ShowDialog(context.resources.getString(R.string.wait))
                    FireStore().UpdateDetailCart(cartlistOld[position].cart_id, context, itemHashMap)
                }
            }
            else if (context is cartlist)
            {
                (context as cartlist).ShowSnackbar(context.resources.getString(R.string.nomoreproduct),false)

            }

        }
    }

    fun setAllProductData(newcartlist: ArrayList<CartDataClass>) {
        var difutils_obj = product_diff_util(newcartlist, cartlistOld)
        var difutils_calucate = DiffUtil.calculateDiff(difutils_obj)
        cartlistOld = newcartlist
        difutils_calucate.dispatchUpdatesTo(this)
    }
}