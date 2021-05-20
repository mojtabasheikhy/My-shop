package com.example.myshop.View.activity

import android.os.Bundle
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myshop.R
import com.example.myshop.View.activity.Basic

import com.example.myshop.databinding.ActivityMymainBinding

class Main : Basic() {


    var dashboardBinding:ActivityMymainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardBinding=DataBindingUtil.setContentView(this,R.layout.activity_mymain)
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.main_background))

        var appbar_configurration =
            AppBarConfiguration(setOf(R.id.navigation_dashboard, R.id.navigation_home, R.id.navigation_notifications
            ,R.id.sold2))

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appbar_configurration)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        CheckDoubleBackToExit(dashboardBinding!!.navView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        return super.onCreateOptionsMenu(menu)
    }


}