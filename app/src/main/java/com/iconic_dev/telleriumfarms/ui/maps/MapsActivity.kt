package com.iconic_dev.telleriumfarms.ui.maps

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.iconic_dev.telleriumfarms.FarmersViewModel
import com.iconic_dev.telleriumfarms.R
import com.iconic_dev.telleriumfarms.db.models.Farmer
import com.iconic_dev.telleriumfarms.utils.ConfirmDialog
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnPolygonClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener{


    private lateinit var farmer: Farmer
    private lateinit var storedfarmerList: MutableList<Farmer>
    val viewModel: FarmersViewModel by viewModel()

    private lateinit var mMap: GoogleMap

    private lateinit var coLList : MutableList<LatLng>
    private lateinit var coordinatesList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        viewModel.storedFarmers!!.observe(this, Observer {
            storedfarmerList = it

        })


        findViewById<ImageView>(R.id.closeFragment).setOnClickListener {
           finish()
        }


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
        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        intent.extras!!["farmer"].let {
            farmer = intent.extras!!["farmer"] as Farmer
            coordinatesList = farmer.coordinates

            val polylist = coordinatesList.map {
                LatLng(
                    it.split(",")[0].toDouble(),
                    it.split(",")[1].toDouble()
                )
            }

            if (polylist.isNotEmpty()){
                val options = PolygonOptions().clickable(true).addList(polylist)
                googleMap.addPolygon(options)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(polylist[0], 13f))
               googleMap.addMarker(
                    MarkerOptions()
                        .position(polylist[0])
                        .title("Click to Save Coordinates")
                        .draggable(true)
                )

            }else{
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(7.2,5.3), 13f))
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(7.2,5.3))
                        .title("Click to Save Coordinates")
                        .draggable(true)
                )

            }

            // Set listeners for click events.
            googleMap.setOnPolygonClickListener(this)
            googleMap.setOnMarkerDragListener(this)
            googleMap.setOnMapLongClickListener(this)
            googleMap.setOnMarkerClickListener(this)


            SweetAlertDialog(this)
                .setTitleText("Info")
                .setContentText(
                    "Long Press when you want to clear current coordinates\nand Plot new ones"
                )
                .show()



        }



    }


    override fun onPolygonClick(p0: Polygon?) {
        p0!!.fillColor = R.color.colorAccent
    }

    fun PolygonOptions.addList(mutableList: List<LatLng>):PolygonOptions{
        mutableList.forEach {
            this.add(it)
        }

        return this
    }

    override fun onMarkerDragStart(p0: Marker?) {
        coLList = mutableListOf()
        coLList.add(p0!!.position)
    }

    override fun onMarkerDrag(p0: Marker?) {

        coLList.add(p0!!.position)
//        for (i in 0 until coLList.size) {

//        }
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        coLList.add(p0!!.position)
        mMap.clear()
        val options = PolygonOptions().clickable(true).addList(coLList)

        mMap.addPolygon(
            options
        )

        mMap.addMarker(
            MarkerOptions()
                .position(coLList.last())
                .title("New Plot")
                .draggable(true)
        )
        Toast.makeText(this, "Tap this Marker to Save Coordinates", Toast.LENGTH_SHORT).show()

//        Toast.makeText(this, "")
    }


    override fun onMapLongClick(p0: LatLng?) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().draggable(true)
            .position(p0!!)
            .title("Drag this Marker to Plot Coordinates"))

        Toast.makeText(this, "Drag this Marker to Plot Coordinates", Toast.LENGTH_SHORT).show()


    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val actions = ConfirmDialog(title = "Update Coordinates For this Farmer?", positiveText = "Save",negativeText = "Cancel" ){
            val  found = storedfarmerList.any { it.farmerId == farmer.farmerId }

            if (coLList.isNotEmpty()){
                val coordinateList =  coLList.map { "${it.latitude},${it.longitude}" }
                farmer.coordinates = coordinateList as ArrayList<String>

                if(found) {
                    val farmerToUpdate = storedfarmerList.filter { it.farmerId == farmer.farmerId }
                    farmerToUpdate[0].coordinates = coordinateList as ArrayList<String>
                    viewModel.updateFarmer(farmerToUpdate[0])
                }
                else viewModel.addFarmer(farmer)
                SweetAlertDialog(this)
                    .setTitleText("Info")
                    .setContentText(
                        "Coordinates\nSaved"
                    )
                    .show()
            }

        }
        actions.show(supportFragmentManager, "")
        return true
    }
}