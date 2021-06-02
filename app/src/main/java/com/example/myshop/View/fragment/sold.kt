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
import com.example.myshop.adapter.SoldAdapter
import com.example.myshop.databinding.FragmentSoldBinding
import com.example.myshop.model.SoldDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class sold : Fragment() {

    lateinit var soldBinding: FragmentSoldBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        soldBinding = FragmentSoldBinding.inflate(layoutInflater, container, false)
        soldBinding.soldSwip.setOnRefreshListener {
            val random=(0..5).random()
            val color= arrayOf(R.color.Golden,R.color.blue,R.color.pink,R.color.green,R.color.orange,R.color.teal_700)
            soldBinding.soldSwip.setColorSchemeResources(color[random])
            CoroutineScope(Dispatchers.IO).launch {
                GetallSoldItem()
            }
            showShimer()
            soldBinding.soldRecycler.visibility=View.VISIBLE
            soldBinding.SoldNodataAnim.visibility=View.GONE
            soldBinding.SoldNodataTv.visibility=View.GONE
        }
        showShimer()
        return soldBinding.root
    }

    override fun onResume() {
        super.onResume()
        GetallSoldItem()
    }
    fun GetallSoldItem(){
        CoroutineScope(Dispatchers.IO).launch {
            FireStore().GetAllSoldMyProduct(this@sold)
        }
    }
    fun failedgetAllsoldOwnList(){
        soldBinding.soldSwip.isRefreshing=false
        hideShimer()
        Toast.makeText(requireContext(),resources.getString(R.string.errorWhenUpadteData),Toast.LENGTH_SHORT).show()
    }

    fun successgetAllproductSold(soldProduct: ArrayList<SoldDataClass>){
        soldBinding.soldSwip.isRefreshing=false
        hideShimer()
        if (soldProduct.size>0) {
            soldBinding.SoldNodataAnim.visibility=View.GONE
            soldBinding.SoldNodataTv.visibility=View.GONE
            soldBinding.soldRecycler.visibility=View.VISIBLE
            var soldAdapter_obj =SoldAdapter(requireContext())

            soldBinding.soldRecycler.apply {
                soldAdapter_obj.setdataSold(soldProduct)
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                adapter = soldAdapter_obj
            }
        }
        else
        {
            soldBinding.SoldNodataAnim.visibility=View.VISIBLE
            soldBinding.SoldNodataTv.visibility=View.VISIBLE
            soldBinding.soldRecycler.visibility=View.GONE
        }

    }


    fun showShimer(){
        soldBinding.soldRecycler.showShimmer()
    }
    fun hideShimer(){
        soldBinding.soldRecycler.hideShimmer()
    }

}