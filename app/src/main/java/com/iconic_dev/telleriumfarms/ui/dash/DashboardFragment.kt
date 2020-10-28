package com.iconic_dev.telleriumfarms.ui.dash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iconic_dev.telleriumfarms.FarmersViewModel
import com.iconic_dev.telleriumfarms.databinding.FragmentDashboardBinding
import org.koin.android.viewmodel.ext.android.viewModel


class DashboardFragment : Fragment() {

    val  binding by lazy { FragmentDashboardBinding.inflate(layoutInflater) }

    val viewModel: FarmersViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.storedFarmers!!.observe(viewLifecycleOwner, Observer{

        })

    }
}