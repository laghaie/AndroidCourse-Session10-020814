package com.example.map

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val REQUEST_CODE = 101

    private var locationManager: LocationManager? = null
    private var locationLister: LocationListener? = null

    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val mapFragment = supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
        mapFragment.getMapAsync(this)*/

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        appUserMap()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


        /*val sematecLatLng = LatLng(35.739161007573394, 51.44333458182703)
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sematecLatLng))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sematecLatLng, 16f))
        googleMap.addMarker(MarkerOptions().position(sematecLatLng).title("Sematec"))*/


        /*locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationLister = LocationListener { location ->
            val userLocation = LatLng(location.latitude, location.longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
            map.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
            locationManager!!.removeUpdates(locationLister!!)
        }
        appUserMap()*/


        val userLocation = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(userLocation).title("$userLocation")
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
        map.addMarker(markerOptions)
    }

    private fun appUserMap() {
        //Location Manager
        /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
         {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
             return
        } else {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0f, locationLister as LocationListener
            )
        }*/


        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            currentLocation = location
            val supportMapFragment =
                (supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment)
            supportMapFragment!!.getMapAsync(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    appUserMap()
            }
        }
    }
}