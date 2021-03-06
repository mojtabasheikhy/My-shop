package com.example.myshop.View.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.adapter.orderAdapter
import com.example.myshop.databinding.FragmentOrderBinding
import com.example.myshop.model.OrderDataClass
import java.util.*


class Order : Fragment() {
lateinit var order:FragmentOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        order= FragmentOrderBinding.inflate(inflater,container,false)
        order.orderSwip.setOnRefreshListener {
            getOrderList()
            var random=(0..5).random()
            var color= arrayOf(R.color.Golden,R.color.blue,R.color.pink,R.color.green,R.color.orange,R.color.teal_700)
            order.orderSwip.setColorSchemeResources(color[random])
            order.orderTextNodata.visibility=View.GONE
            order.orderAnim.visibility=View.GONE
            order.orderRecyclerview.visibility=View.VISIBLE


        }
        return order.root
    }

    override fun onResume() {
        getOrderList()
        super.onResume()

    }

    fun getOrderList(){
        showshimer()
        FireStore().getAllorder(this)
    }

    fun successGetAllOrder(myorderlist: ArrayList<OrderDataClass>) {
        hideshimer()
        order.orderSwip.isRefreshing=false

      if (myorderlist.size.equals(0)){
          order.orderTextNodata.visibility=View.VISIBLE
          order.orderAnim.visibility=View.VISIBLE
          order.orderSwip.visibility=View.VISIBLE
          order.orderRecyclerview.visibility=View.GONE
      }
        else {
          order.orderTextNodata.visibility=View.GONE
          order.orderAnim.visibility=View.GONE
          order.orderSwip.visibility=View.VISIBLE
          order.orderRecyclerview.visibility=View.VISIBLE
          var size_notifiaction=myorderlist.size
          order.orderRecyclerview.apply {

              var adapter_order = orderAdapter(requireContext())
              adapter = adapter_order
              adapter_order.setAllOrderData(myorderlist)
              layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
          }
      }


    }

    fun hideshimer(){
        order.orderRecyclerview.hideShimmer()
    }
    fun showshimer(){
        order.orderRecyclerview.showShimmer()

    }
    fun failedGetAllorder(){
        Toast.makeText(requireContext(),resources.getString(R.string.failgetAllOrder),Toast.LENGTH_SHORT).show()
    }
}