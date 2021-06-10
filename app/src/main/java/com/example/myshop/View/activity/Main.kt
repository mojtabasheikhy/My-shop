package com.example.myshop.View.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityMymainBinding
import com.example.myshop.model.OrderDataClass
import com.example.myshop.model.ProductDataClass
import com.example.myshop.model.SoldDataClass
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt.PromptStateChangeListener
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal
import java.util.*


class Main : Basic(), BottomNavigationView.OnNavigationItemSelectedListener {

    var badageDrawable_order: BadgeDrawable? = null
    var badageDrawable_sold: BadgeDrawable? = null
    var badageDrawable_ownproduct: BadgeDrawable? = null
    lateinit var pref: SharedPreferences
    var mainBinding: ActivityMymainBinding? = null
    var navController:NavController? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_mymain)
        pref = getSharedPreferences(ConstVal.fristLogin, Context.MODE_PRIVATE)
        setup_toolbar()

       lifecycleScope.launch(Dispatchers.IO){
        admob()
        }

        TourGuide()
        lifecycleScope.launch {
            FireStore().getAllorderMain(this@Main)
            FireStore().GetMyProductmain(this@Main)
            FireStore().GetAllSoldMyProductmain(this@Main)
        }
        notifiactionbottomAppbar()
    }
    suspend fun admob() {
          var adRequest:AdRequest?=null
          MobileAds.initialize(this) {}
          val adView = AdView(this)
          adView.adSize = AdSize.BANNER
          adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
          lifecycleScope.launch(Dispatchers.IO){
          adRequest = AdRequest.Builder().build()
              withContext(Dispatchers.Main){mainBinding?.adView?.loadAd(adRequest)}
          }

    }
    fun notifiactionbottomAppbar(){
        badageDrawable_order = mainBinding?.navView!!.getOrCreateBadge(R.id.navigation_order)
        badageDrawable_sold = mainBinding?.navView!!.getOrCreateBadge(R.id.navigation_sold)
        badageDrawable_ownproduct = mainBinding?.navView!!.getOrCreateBadge(R.id.navigation_product)

        badageDrawable_order?.backgroundColor = (ContextCompat.getColor(this, R.color.white))
        badageDrawable_order?.badgeTextColor = ContextCompat.getColor(this, R.color.pink)

        badageDrawable_ownproduct?.backgroundColor = (ContextCompat.getColor(this, R.color.white))
        badageDrawable_ownproduct?.badgeTextColor = ContextCompat.getColor(this, R.color.pink)

        badageDrawable_sold?.backgroundColor = (ContextCompat.getColor(this, R.color.white))
        badageDrawable_sold?.badgeTextColor = ContextCompat.getColor(this, R.color.pink)

        badageDrawable_sold?.setVisible(false)
        badageDrawable_order?.setVisible(false)
        badageDrawable_ownproduct?.setVisible(false)


    }
    fun setup_toolbar(){
        supportActionBar?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.main_background
            )
        )

        var appbar_configurration =
            AppBarConfiguration(
                setOf(
                    R.id.navigation_product,
                    R.id.navigation_home,
                    R.id.navigation_order,
                    R.id.navigation_sold
                )
            )
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController!!, appbar_configurration)
        navView.setOnNavigationItemSelectedListener(this)
    }
    override fun onBackPressed() {
        CheckDoubleBackToExit(mainBinding!!.navView)
    }
    fun showpromt_addproduct() {
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.product_add_new)
            .setFocalColour(ContextCompat.getColor(this, R.color.pink))
            .setBackgroundColour(ContextCompat.getColor(this, R.color.orange))
            .setPrimaryText(resources.getString(R.string.addnewproduct_main))
            .setBackButtonDismissEnabled(false)
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    showpromt_order()
                    mainBinding?.navView?.selectedItemId = R.id.navigation_order
                    // User has pressed the prompt target
                }
            })
            .show()
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
                        Log.e("s", "0")
                        // User has pressed the prompt target
                    }
                })
                .show()
        }
    }
    fun showpromt_product() {

        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.navigation_product)
            .setFocalColour(ContextCompat.getColor(this, R.color.pink))
            .setBackgroundColour(ContextCompat.getColor(this, R.color.orange))
            .setPrimaryText(resources.getString(R.string.tour_product_main))
            .setSecondaryText(resources.getString(R.string.tour_product))
            .setBackButtonDismissEnabled(false)
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    showpromt_addproduct()
                    Log.e("s", "1")

                    // User has pressed the prompt target
                }
            })
            .show()
    }
    fun showpromt_order() {

        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.navigation_order)
            .setFocalColour(ContextCompat.getColor(this, R.color.pink))
            .setBackgroundColour(ContextCompat.getColor(this, R.color.orange))
            .setPrimaryText(resources.getString(R.string.tour_order_main))
            .setSecondaryText(resources.getString(R.string.tour_order))
            .setBackButtonDismissEnabled(false)
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    showpromt_sold()
                    Log.e("s", "2")
                    mainBinding?.navView?.selectedItemId = R.id.navigation_sold
                    // User has pressed the prompt target
                }
            })
            .show()
    }
    fun showpromt_sold() {
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.navigation_sold)
            .setFocalColour(ContextCompat.getColor(this, R.color.pink))
            .setBackgroundColour(ContextCompat.getColor(this, R.color.orange))
            .setPrimaryText(resources.getString(R.string.tour_sold_main))
            .setSecondaryText(resources.getString(R.string.tour_sold))
            .setBackButtonDismissEnabled(false)
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    showpromt_profile()
                    Log.e("s", "3")
                    val editor = pref.edit()
                    Log.e("s", "3")
                    editor.putBoolean(ConstVal.fristLoginYes, true)
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
                    mainBinding?.navView?.selectedItemId = R.id.navigation_product

                    // User has pressed the prompt target
                }
            })
            .setPromptFocal(RectanglePromptFocal())
            .show()
    }
    override fun onDestroy() {
        super.onDestroy()
        mainBinding = null
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_order -> {

                badageDrawable_order?.clearNumber()
                badageDrawable_order?.setVisible(false)
                navController?.navigate(R.id.navigation_order)
                return true
            }
            R.id.navigation_home -> {

                navController?.navigate(R.id.navigation_home)
                return true
            }
            R.id.navigation_product -> {

                badageDrawable_ownproduct?.clearNumber()
                badageDrawable_ownproduct?.setVisible(false)
                navController?.navigate(R.id.navigation_product)
                return true
            }
            R.id.navigation_sold -> {

                badageDrawable_sold?.clearNumber()
                badageDrawable_sold?.setVisible(false)
                navController?.navigate(R.id.navigation_sold)

                return true
            }
            else -> {
                return false
            }
        }
    }
    fun successGetAllOrder(myorderlist: ArrayList<OrderDataClass>) {
        if (myorderlist.size==0){
            badageDrawable_order?.setVisible(false)
        }
        else{
            badageDrawable_order?.setVisible(true)
            badageDrawable_order?.number = myorderlist.size
        }
    }
    fun failedGetAllorder() {
            Log.e("faild","failed to get all order")
    }
    fun successGetMyProductFromFireStore(myProductList: ArrayList<ProductDataClass>) {
        if (myProductList.size==0){
            badageDrawable_ownproduct?.setVisible(false)
        }
        else{
            badageDrawable_ownproduct?.setVisible(true)
            badageDrawable_ownproduct?.number = myProductList.size
        }
    }
    fun failedgetproduct() {
        Log.e("faild","failed to get your product")
    }
    fun successgetAllproductSold(myproductSoldList: ArrayList<SoldDataClass>) {
        if (myproductSoldList.size==0){
            badageDrawable_sold?.setVisible(false)
        }
        else{
            badageDrawable_sold?.setVisible(true)
            badageDrawable_sold?.number = myproductSoldList.size
        }
    }
    fun failedgetAllsoldOwnList() {
        Log.e("faild","failed to get your sold")
    }

}


