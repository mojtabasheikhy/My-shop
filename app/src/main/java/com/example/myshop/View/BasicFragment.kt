package com.example.myshop.View

import android.app.AlertDialog
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout


open class BasicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic, container, false)
    }

    fun show_snackbar(message: String, boolean: Boolean) {
        var snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        if (boolean) {
            snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green))
        } else
            snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))

                snackbar.setAnchorView(R.id.nav_view)
                snackbar.show()
    }

    fun ShowAlertDialog(title:String, message: String, icon:Int): AlertDialog.Builder{
        var alerdialog = AlertDialog.Builder(requireActivity())
            .setTitle(title)

            .setMessage(message)
            .setIcon(icon)
        return alerdialog
    }


}