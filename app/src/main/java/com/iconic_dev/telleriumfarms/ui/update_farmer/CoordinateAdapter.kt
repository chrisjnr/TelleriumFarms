package com.iconic_dev.telleriumfarms.ui.update_farmer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iconic_dev.telleriumfarms.R
import java.util.ArrayList

/**
 * Created by manuelchris-ogar on 26/10/2020.
 */
class CoordinateAdapter(val coordinatesList: ArrayList<String>): RecyclerView.Adapter<CoordinateAdapter.Viewholder>() {
    class Viewholder(view: View):RecyclerView.ViewHolder(view) {
        val longitude = view.findViewById<TextView>(R.id.longitude)
        val latitude = view.findViewById<TextView>(R.id.latitude)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val v =LayoutInflater.from(parent.context).inflate(R.layout.new_coordinate_viewholder, parent, false)

            return Viewholder(v)
        }

    override fun getItemCount(): Int {
        return coordinatesList.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val coordinates = coordinatesList[position]

        val longitude = coordinates.split(",")[0].substring(0,6)

        holder.longitude.text = "Longitude: $longitude"

        val latitude = coordinates.split(",")[1].substring(0,6)

        holder.latitude.text ="Longitude: $latitude"
    }
}