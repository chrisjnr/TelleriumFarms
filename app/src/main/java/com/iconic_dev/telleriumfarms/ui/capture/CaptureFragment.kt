package com.iconic_dev.telleriumfarms.ui.capture

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iconic_dev.telleriumfarms.FarmersViewModel
import com.iconic_dev.telleriumfarms.R
import com.iconic_dev.telleriumfarms.databinding.FragmentCaptureBinding
import com.iconic_dev.telleriumfarms.db.models.Farmer
import org.koin.android.viewmodel.ext.android.viewModel


class CaptureFragment : Fragment() {



    private lateinit var farmer: Farmer

    val viewModel: FarmersViewModel by viewModel()

    lateinit var adapter : SavedFarmersAdapter

    private lateinit var storedfarmerList: MutableList<Farmer>
    private  val REQUEST_PERMISSION_LOCATION = 100


    val binding by lazy { FragmentCaptureBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeFragment.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host).popBackStack()
        }
        adapter = SavedFarmersAdapter("", object :   SavedFarmersAdapter.onClickListener{
            override fun viewMore(selectedFarmer: Farmer) {

                val found = storedfarmerList.filter{eachFarmer -> eachFarmer.farmerId ==selectedFarmer.farmerId}

                farmer = if (found.isNotEmpty()) found[0] else selectedFarmer

                val bundle = bundleOf("farmer" to farmer  ,  "base_url" to adapter.base_url)

                confirmPermissions(bundle)

            }

        })

        viewModel.storedFarmers!!.observe(viewLifecycleOwner, Observer{


            storedfarmerList = it
            if (it.isNotEmpty()) binding.emptyList.visibility  = View.GONE
            adapter.submitList(it)
            binding.capturedFarmersRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.capturedFarmersRecyclerView.adapter = adapter
        })


    }

    private fun confirmPermissions(bundle: Bundle) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( // request permission
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION ),
                REQUEST_PERMISSION_LOCATION
            )
        } else {
            requireActivity().findNavController(R.id.nav_host).navigate(R.id.updateFarmerFragment, bundle)

        }
    }

}