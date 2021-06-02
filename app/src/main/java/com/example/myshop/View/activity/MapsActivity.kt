package com.example.myshop.View.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivityMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var mMap: GoogleMap
    var buyyersydney: LatLng? = null
    var map_Binding: ActivityMapsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        map_Binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map_Binding?.MapFindLocation?.setOnClickListener(this)

        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.apply {
            isMapToolbarEnabled = true
            isMyLocationButtonEnabled = true
            isZoomControlsEnabled = true
            isTiltGesturesEnabled = true
        }
        mMap.isMyLocationEnabled=true
        val locationServices = LocationServices.getFusedLocationProviderClient(this)
        val task = locationServices.lastLocation
        task.addOnSuccessListener {
            buyyersydney = LatLng(it.latitude, it.longitude)
            mMap.addMarker(MarkerOptions().position(buyyersydney).title("Marker in Sydney").draggable(true))
                .setIcon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                )

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(buyyersydney,250f))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Map_Find_location -> {
                if (buyyersydney != null) {
                    try {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(buyyersydney!!.latitude, buyyersydney!!.longitude, 1)
                        if (addresses != null) {
                            val address = addresses.get(0).getAddressLine(0)
                            val city = addresses.get(0).getLocality()
                            val state = addresses.get(0).getAdminArea()
                            val country = addresses.get(0).getCountryName()
                            val postalCode = addresses.get(0).getPostalCode()
                            val knownName = addresses.get(0).getFeatureName()
                            setResult(RESULT_OK, Intent().putStringArrayListExtra("address", arrayListOf(address,city,state,country,postalCode,knownName)))
                            finish()
                            }
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    class resulatBack : ActivityResultContract<SuccessGettingLocation, outputAddress>() {
        override fun createIntent(context: Context, input: SuccessGettingLocation?): Intent =
            Intent(context, MapsActivity::class.java)

        override fun parseResult(resultCode: Int, intent: Intent?): outputAddress =
            outputAddress(success = resultCode == Activity.RESULT_OK, message = "success Getting Data", arrayList = intent?.getStringArrayListExtra("address")!!)
    }

}

class SuccessGettingLocation() {}
class outputAddress(var success: Boolean, var message: String, var arrayList: java.util.ArrayList<String>)