package com.example.myshop.View.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.View.activity.DetailProduct
import com.example.myshop.View.activity.Settings
import com.example.myshop.View.activity.cartlist
import com.example.myshop.adapter.DashbordAdapter
import com.example.myshop.databinding.FragmentDashbordBinding
import com.example.myshop.model.ProductDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class Dashbord : Fragment() {
    lateinit var pref:SharedPreferences
    lateinit var dashbordBinding: FragmentDashbordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = requireActivity().getSharedPreferences(ConstVal.fristLogin, Context.MODE_PRIVATE)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dashbordBinding = FragmentDashbordBinding.inflate(inflater, container, false)
        dashbordBinding.dashbordSwiprefresh.setOnRefreshListener {
            dashbordBinding.dashbordProductNoData.visibility = View.GONE
            dashbordBinding.lottieAnimationView3.visibility = View.GONE
            dashbordBinding.dashbordRecycler.visibility = View.VISIBLE
            showshimer()
            GetAllProduct()
        }
        return dashbordBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashbord_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onResume() {
        GetAllProduct()
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dashbord_menu_settings -> {
                   val intent = Intent(activity, Settings::class.java)
                   startActivity(intent)
                   return true
            }
            R.id.dashbord_menu_cart -> {
                    val intent_cart = Intent(activity, cartlist::class.java)
                    startActivity(intent_cart)
                    return true
            }
        }
        return super.onOptionsItemSelected(item)

    }

     fun GetAllProduct() {
        showshimer()
        CoroutineScope(IO).launch {
            FireStore().GetAllProduct(this@Dashbord)
        }
    }

    fun successGetAllProductFromFireStore(AllProductList: ArrayList<ProductDataClass>) {
        hideshimer()
        dashbordBinding.dashbordSwiprefresh.isRefreshing = false

        val productAdapter = DashbordAdapter()
        productAdapter.setOnclickListener(object : DashbordAdapter.click {
            override fun onclickListener(productDataClass: ProductDataClass, position: Int) {
                val intent = Intent(context, DetailProduct::class.java)
                intent.putExtra(ConstVal.putExtera_detail_product, productDataClass.product_id)
                intent.putExtra(ConstVal.PutExtera_detail_userid, productDataClass.user_id_Seller)
                startActivity(intent)
            }

        })
        if (AllProductList.size == 0) {
            dashbordBinding.dashbordProductNoData.visibility = View.VISIBLE
            dashbordBinding.lottieAnimationView3.visibility = View.VISIBLE
            dashbordBinding.dashbordRecycler.visibility = View.GONE

        }
        if (AllProductList.size > 0) {
            productAdapter.setAllProductData(AllProductList)
            dashbordBinding.dashbordProductNoData.visibility = View.GONE
            dashbordBinding.lottieAnimationView3.visibility = View.GONE
            dashbordBinding.dashbordRecycler.apply {
                setHasFixedSize(true)
                visibility = View.VISIBLE
                layoutManager = GridLayoutManager(activity?.applicationContext, 2)
                adapter = productAdapter
            }

        }


    }

    fun showshimer() {
        dashbordBinding.dashbordRecycler.showShimmer()
        dashbordBinding.dashbordRecycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity?.applicationContext, 2)
        }
    }

    fun hideshimer() {
        dashbordBinding.dashbordRecycler.hideShimmer()
    }
}
