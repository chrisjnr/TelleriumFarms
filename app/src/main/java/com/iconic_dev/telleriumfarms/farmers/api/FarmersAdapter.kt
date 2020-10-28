

package com.iconic_dev.telleriumfarms.farmers.api

import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.iconic_dev.telleriumfarms.db.models.Farmer

class FarmersAdapter(var base_url:String, val clickListener:  onClickListener) : PagedListAdapter<Farmer, FarmersViewHolder>(diffCallback) {
    interface onClickListener{
        fun  viewMore(farmer: Farmer)
    }


    override fun onBindViewHolder(holder: FarmersViewHolder, position: Int) {
        holder.bindTo(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmersViewHolder =
            FarmersViewHolder(base_url,parent,clickListener)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Farmer>() {
            override fun areItemsTheSame(oldItem: Farmer, newItem: Farmer): Boolean =
                    oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Farmer, newItem: Farmer): Boolean =
                    oldItem == newItem
        }
    }
}
