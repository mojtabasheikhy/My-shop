package com.example.myshop.View.activity

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.lottie.animation.content.RectangleContent
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityMymainBinding

import com.google.android.material.bottomnavigation.BottomNavigationView
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt.PromptStateChangeListener
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal


class Main : Basic() {

    lateinit var pref:SharedPreferences
    var dashboardBinding: ActivityMymainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_mymain)
        pref = getSharedPreferences(ConstVal.fristLogin, Context.MODE_PRIVATE)

        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.main_background))

        var appbar_configurration =
            AppBarConfiguration(setOf(R.id.navigation_dashboard, R.id.navigation_home, R.id.navigation_notifications, R.id.sold2))

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appbar_configurration)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setupWithNavController(navController)
        TourGuide()
    }

    override fun onBackPressed() {
        CheckDoubleBackToExit(dashboardBinding!!.navView)
    }


    fun TourGuide() {

        if (!pref.getBoolean(ConstVal.fristLoginYes, false)) {
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.navigation_home)
                .setBackButtonDismissEnabled(false)
                .setPrimaryText(resources.getString(R.string.tour_home_main))
                .setBackgroundColour(ContextCompat.getColor(this, R.color.orange))
                .setSecondaryText(resources.getString(R.string.tour_home))
                .setFocalColour(ContextCompat.getColor(this, R.color.pink))
                .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                        showpromt_profile()
                        Log.e("s","0")
                        // User has pressed the prompt target
                    }
                })
                .show()
        }
    }

    fun showpromt_product() {

        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.navigation_dashboard)
            .setFocalColour(ContextCompat.getColor(this, R.color.pink))
            .setBackgroundColour(ContextCompat.getColor(this, R.color.orange))
            .setPrimaryText(resources.getString(R.string.tour_product_main))
            .setSecondaryText(resources.getString(R.string.tour_product))
            .setBackButtonDismissEnabled(false)
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    showpromt_order()
                    Log.e("s","1")
                    dashboardBinding?.navView?.selectedItemId = R.id.navigation_notifications
                    // User has pressed the prompt target
                }
            })
            .show()
    }

    fun showpromt_order() {

        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.navigation_notifications)
            .setFocalColour(ContextCompat.getColor(this, R.color.pink))
            .setBackgroundColour(ContextCompat.getColor(this, R.color.orange))
            .setPrimaryText(resources.getString(R.string.tour_order_main))
            .setSecondaryText(resources.getString(R.string.tour_order))
            .setBackButtonDismissEnabled(false)
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    showpromt_sold()
                    Log.e("s","2")
                    dashboardBinding?.navView?.selectedItemId = R.id.sold2
                    // User has pressed the prompt target
                }
            })
            .show()
    }

    fun showpromt_sold() {


        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.sold2)
            .setFocalColour(ContextCompat.getColor(this, R.color.pink))
            .setBackgroundColour(ContextCompat.getColor(this, R.color.orange))
            .setPrimaryText(resources.getString(R.string.tour_sold_main))
            .setSecondaryText(resources.getString(R.string.tour_sold))
            .setBackButtonDismissEnabled(false)
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    showpromt_profile()
                    Log.e("s","3")
                    val editor = pref.edit()
                    Log.e("s","3")
                    editor.putBoolean(ConstVal.fristLoginYes,true)
                    editor.apply()
                    // User has pressed the prompt target
                }
            })
            .show()
    }

    private fun showpromt_profile() {
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.dashbord_menu_settings)
            .setBackButtonDismissEnabled(false)
            .setFocalColour(ContextCompat.getColor(this, R.color.pink))
            .setBackgroundColour(ContextCompat.getColor(this, R.color.orange))
            .setPrimaryText(resources.getString(R.string.tour_settins_main))
            .setSecondaryText(resources.getString(R.string.tour_settins))
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    showpromt_cart()
                    // User has pressed the prompt target
                }
            })
            .show()
    }

    private fun showpromt_cart() {
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.dashbord_menu_cart)
            .setFocalColour(ContextCompat.getColor(this, R.color.pink))
            .setBackgroundColour(ContextCompat.getColor(this, R.color.orange))
            .setPrimaryText(resources.getString(R.string.tour_cart_main))
            .setSecondaryText(resources.getString(R.string.tour_cart))
            .setBackButtonDismissEnabled(true)
            .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                      showpromt_product()
                        dashboardBinding?.navView?.selectedItemId = R.id.navigation_dashboard

                        // User has pressed the prompt target
                    }
                })
            .setPromptFocal(RectanglePromptFocal())
            .show()
    }

}


