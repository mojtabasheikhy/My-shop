package com.example.myshop.View.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.AlertDialogWaitingBinding
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty

open class Basic : AppCompatActivity() {
     var alertDialog:Dialog? = null
    var DoubleBackClickToExit = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)
    }

    fun ShowSnackbar(message: String ,boolean: Boolean){
        val snackbar=Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT)
        val snackbarview=snackbar.view
        if (boolean)
            snackbarview.setBackgroundColor(ContextCompat.getColor(this,R.color.green))
        else
            snackbarview.setBackgroundColor(ContextCompat.getColor(this,R.color.red))
        snackbar.show()
    }
    fun ShowDialog(message: String){
        val dialogBinding: AlertDialogWaitingBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.alert_dialog_waiting,
                null,
                false
            )
        dialogBinding?.alertTvLoading?.text=message
        alertDialog =Dialog(this)
        alertDialog!!.setContentView(dialogBinding!!.root)
        alertDialog!!.setCancelable(false)
        val typeface_alert =Typeface.createFromAsset(assets,"fonts/"+"iranyekanweblight2.ttf")
        dialogBinding.alertTvLoading.typeface = typeface_alert
        alertDialog!!.setCanceledOnTouchOutside(false)
        alertDialog!!.show()
    }
    fun HideDialog(){
        if (alertDialog != null) {
            alertDialog?.dismiss()
        }

    }
    fun CheckDoubleBackToExit(view: View) {
        if (DoubleBackClickToExit + 2000 > System.currentTimeMillis()) {
            finish()
        } else {

            Snackbar.make(findViewById(android.R.id.content),resources.getString(R.string.backtoExit),Snackbar.LENGTH_SHORT)
                .setAnchorView(view)
                .show()
            DoubleBackClickToExit = System.currentTimeMillis()
        }
    }
    fun Custom_Toast(context:Context,message: String,icon:Int?,color:Int){
        val typeface_Toast =Typeface.createFromAsset(assets,"fonts/"+"iranyekanweblight2.ttf")
        Toasty.Config.getInstance()
            .setToastTypeface(typeface_Toast) // optional
            .setTextSize(12) // optional
            .allowQueue(true) // optional (prevents several Toastys from queuing)
            .apply()
       var toast= Toasty.custom(context,message, icon!!, color,Toast.LENGTH_SHORT,true,true).show()

    }


}