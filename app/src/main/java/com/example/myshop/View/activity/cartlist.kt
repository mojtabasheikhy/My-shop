package com.example.myshop.View.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivityCartlistBinding

class cartlist : AppCompatActivity() {
    var cartListBinding:ActivityCartlistBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartListBinding = DataBindingUtil.setContentView(this,R.layout.activity_cartlist)
        actionbarSetup()

    }
    fun actionbarSetup() {
        setSupportActionBar(cartListBinding?.cartlistToolbar)
        val actionbar_history = supportActionBar
        if (actionbar_history != null) {
            actionbar_history.setDisplayHomeAsUpEnabled(true)
            actionbar_history.title = resources.getString(R.string.cartlist)
        }
        cartListBinding?.cartlistToolbar?.setNavigationIcon(R.drawable.ic_back)

        cartListBinding?.cartlistToolbar?.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }
}