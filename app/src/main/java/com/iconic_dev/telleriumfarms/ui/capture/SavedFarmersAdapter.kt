

package com.iconic_dev.telleriumfarms.ui.capture

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iconic_dev.telleriumfarms.R
import com.iconic_dev.telleriumfarms.db.models.Farmer
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.joda.time.LocalDate
import org.joda.time.Years

class SavedFarmersAdapter(var base_url:String, val clickListener:  SavedFarmersAdapter.onClickListener) : ListAdapter<Farmer, SavedFarmersAdapter.SavedFarmersViewHolder>(diffCallback) {
    public interface onClickListener{
        fun  viewMore(farmer: Farmer)
    }


    override fun onBindViewHolder(holder: SavedFarmersAdapter.SavedFarmersViewHolder, position: Int) {
        holder.bindTo(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedFarmersAdapter.SavedFarmersViewHolder =
        SavedFarmersViewHolder(base_url,parent,clickListener)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Farmer>() {
            override fun areItemsTheSame(oldItem: Farmer, newItem: Farmer): Boolean =
                    oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Farmer, newItem: Farmer): Boolean =
                    oldItem == newItem
        }
    }

    public class SavedFarmersViewHolder(
        val base_url: String,
        parent: ViewGroup,
        val  clickListener: SavedFarmersAdapter.onClickListener
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.saved_farmers_view_holder, parent, false)) {

        private val farmerName = itemView.findViewById<TextView>(R.id.farmerName)
        private val farmerImage = itemView.findViewById<ImageView>(R.id.farmerImage)
        private val marital_status = itemView.findViewById<TextView>(R.id.marital_status)
        private val occupation = itemView.findViewById<TextView>(R.id.occupation)
        private val farmerAge = itemView.findViewById<TextView>(R.id.farmerAge)
        private val address = itemView.findViewById<TextView>(R.id.address)
        private val id_type = itemView.findViewById<TextView>(R.id.id_type)


        var farmer : Farmer? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(farmer : Farmer) {
            this.farmer = farmer
            farmerName.text = "${farmer.firstName.toLowerCase().capitalize()} ${farmer.surname.toLowerCase().capitalize()}"

            itemView.setOnClickListener {
                clickListener.viewMore(farmer)
            }


            marital_status.text = farmer.maritalStatus
            occupation.text = farmer.occupation
            address.text  = "Address: ${farmer.address.toLowerCase().capitalize()} ${farmer.city.toLowerCase().capitalize()}"
            id_type.text = "ID Type: ${farmer.idType}"



//            location.text = farmer.address
//            phone_number.text = farmer.mobileNo

            val dob = farmer.dob.split("-")

            val birthDate = LocalDate(dob[0].toInt(), dob[1].toInt(),dob[2].toInt())

            val age = calculateAge(birthDate)


            farmerAge.text   = "$age"

            val thumb: String = "$base_url${farmer.passportPhoto}"

            Log.e("PPPP", thumb)

            val placeholder = if (farmer.gender=="Female")  R.drawable.ic_woman else R.drawable.ic_man

            try {
                if (!TextUtils.isEmpty(thumb)) Picasso.get().load(thumb)
                    .fit()
                    .centerCrop()
                    .placeholder(placeholder)
                    .into(farmerImage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun calculateAge(birthDate: LocalDate):Int {
            val now = LocalDate()
            return Years.yearsBetween(birthDate, now).years
        }
    }
}
