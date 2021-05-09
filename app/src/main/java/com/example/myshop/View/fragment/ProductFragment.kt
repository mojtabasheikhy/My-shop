package com.example.myshop.View.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.model.ProductDataClass
import com.example.myshop.R
import com.example.myshop.View.BasicFragment
import com.example.myshop.View.activity.AddProduct
import com.example.myshop.View.activity.Basic
import com.example.myshop.adapter.Product_adapter
import com.example.myshop.databinding.FragmentProductBinding

class ProductFragment : BasicFragment() {


    lateinit var ProductBinding: FragmentProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        ProductBinding = FragmentProductBinding.inflate(inflater, container, false)
        ProductBinding.productSwiprefresh.setOnRefreshListener {
            ProductBinding.ProductNoData.visibility=View.GONE
            showshimer()
            GetMyProductFromFireStore()
        }

        return ProductBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.product_add_new -> {
                var intent_addnew = Intent(activity, AddProduct::class.java)
                startActivity(intent_addnew)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


    fun GetMyProductFromFireStore() {
        FireStore().GetMYProduct(this)
    }

    override fun onResume() {
        showshimer()
        GetMyProductFromFireStore()
        super.onResume()

    }

    fun successGetMyProductFromFireStore(MyProductList: ArrayList<ProductDataClass>) {
        hideshimer()
        ProductBinding.productSwiprefresh.isRefreshing = false
        var productAdapter = Product_adapter(requireContext(),this)
        productAdapter.setdata(MyProductList)

        if (MyProductList.size==0){
            ProductBinding.ProductNoData.visibility = View.VISIBLE

        }
        if (MyProductList.size > 0) {
            ProductBinding.ProductNoData.visibility = View.GONE
        }
        ProductBinding.productSwiprefresh.visibility = View.VISIBLE
        ProductBinding.ProdcutRecycler.apply {
            setHasFixedSize(true)
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = productAdapter

        }
    }

    fun showshimer() {
        ProductBinding.ProdcutRecycler.showShimmer()
    }

    fun hideshimer() {
        ProductBinding.ProdcutRecycler.hideShimmer()
    }

    fun SuccessDeleteMyProduct() {
        show_snackbar(resources.getString(R.string.deletedSuccessFully),true)
        GetMyProductFromFireStore()

    }

    fun failedDeleteMyProduct() {
        show_snackbar(resources.getString(R.string.deletedfailed),true)

    }

    fun showalertDelete(productId: String) {
        var alert = ShowAlertDialog(
            resources.getString(R.string.deleteitem),
            resources.getString(R.string.Are_you_sure),R.drawable.ic_delete
        )
        alert.setPositiveButton(resources.getString(R.string.yes),{ dialog: DialogInterface?, which: Int ->
            //TODO delete item
          FireStore().deleteMyProduct(this,productId)
        })
        alert.setNegativeButton(resources.getString(R.string.no),{dialog: DialogInterface?, which: Int ->
            //TODO dismis alert
            dialog?.dismiss()

        })
        alert.create()
        alert.setCancelable(false)
        alert.show()




    }

    fun deleteItem(productId: String){
        showalertDelete(productId)
    }



}