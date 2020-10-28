package com.iconic_dev.telleriumfarms.farmers.api

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iconic_dev.telleriumfarms.R
import com.iconic_dev.telleriumfarms.db.models.Farmer
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.joda.time.LocalDate
import org.joda.time.Years


/**
 * A simple ViewHolder that can bind a Farmer item. It also accepts null items since the data may
 * not have been fetched before it is bound.
 */
class FarmersViewHolder(
    val base_url: String,
    parent: ViewGroup,
    val  clickListener: FarmersAdapter.onClickListener
) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.farmer_item, parent, false)) {

    private val nameView = itemView.findViewById<TextView>(R.id.name)
    private val progressBar = itemView.findViewById<ProgressBar>(R.id.progress)
    private val farmerImage = itemView.findViewById<ImageView>(R.id.shop_icon)
    private val location = itemView.findViewById<TextView>(R.id.location)
    private val phone_number = itemView.findViewById<TextView>(R.id.phone_number)
    private val farmerAge = itemView.findViewById<TextView>(R.id.farmerAge)
    private val viewMore = itemView.findViewById<TextView>(R.id.view_more)

    var farmer : Farmer? = null

    /**
     * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
     * ViewHolder when Item is loaded.
     */
    fun bindTo(farmer : Farmer) {
        this.farmer = farmer
        nameView.text = "${farmer.firstName.toLowerCase().capitalize()} ${farmer.surname.toLowerCase().capitalize()}"

        viewMore.setOnClickListener {
            clickListener.viewMore(farmer)
        }


        location.text = farmer.address
        phone_number.text = farmer.mobileNo

        val dob = farmer.dob.split("-")

        val birthDate = LocalDate(dob[0].toInt(), dob[1].toInt(),dob[2].toInt())

        val age = calculateAge(birthDate)


        farmerAge.text   = "Age : $age"

        val thumb: String = "$base_url${farmer.passportPhoto}"


        val placeholder = if (farmer.gender=="Female")  R.drawable.ic_woman else R.drawable.ic_man

        try {
            if (!TextUtils.isEmpty(thumb)) Picasso.get().load(thumb)
                .fit()
                .centerCrop()
                .placeholder(placeholder)
                .into(farmerImage, object : Callback {
                    override fun onSuccess() {
                        progressBar.setVisibility(View.GONE)
                    }

                    override fun onError(e: Exception) {
                        progressBar.setVisibility(View.GONE)
                    }
                }) else {
                progressBar.setVisibility(View.GONE)
//                shop_icon.setImageDrawable(app.getDrawable(R.drawable.ee_min))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun calculateAge(birthDate: LocalDate):Int {
        val now = LocalDate()
        return Years.yearsBetween(birthDate, now).years
    }
}