package com.iconic_dev.telleriumfarms.ui.maps

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.iconic_dev.telleriumfarms.R
import com.iconic_dev.telleriumfarms.db.models.Farmer


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
    GoogleMap.OnPolygonClickListener {

    private lateinit var mMap: GoogleMap

    private lateinit var coordinatesList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

//        Log.e("PPP", intent.("farmer")!!.keySet().toString())
//        val builder = StringBuilder("Extras:\n")
//        for (key in intent.extras!!.keySet()) { //extras is the Bundle containing info
//            val value = intent!!.extras!![key]?.toString() //get the current object
//            builder.append(key).append(": ").append(value)
//                .append("\n") //add the key-value pair to the
//        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        intent.extras!!["farmer"].let {
            val farmer = intent.extras!!["farmer"] as Farmer
            coordinatesList = farmer.coordinates

            val polylist = coordinatesList.map {
                LatLng(
                    it.split(",")[0].toDouble(),
                    it.split(",")[1].toDouble()
                )
            }

            val options = PolylineOptions().clickable(true).addList(polylist)
            val polyline1 = googleMap.addPolyline(options)


            // Position the map's camera near Alice Springs in the center of Australia,
            // and set the zoom factor so most of Australia shows on the screen.

            // Position the map's camera near Alice Springs in the center of Australia,
            // and set the zoom factor so most of Australia shows on the screen.
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.684, 133.903), 4f))

            // Set listeners for click events.

            // Set listeners for click events.
            googleMap.setOnPolylineClickListener(this)
            googleMap.setOnPolygonClickListener(this)


        }



    }

    override fun onPolylineClick(p0: Polyline?) {

    }

    override fun onPolygonClick(p0: Polygon?) {
    }

    fun PolylineOptions.addList(mutableList: List<LatLng>):PolylineOptions{
        mutableList.forEach {
            this.add(it)
        }

        return this
    }
}