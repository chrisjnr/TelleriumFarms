package com.iconic_dev.telleriumfarms.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.flatdialoglibrary.dialog.FlatDialog
import com.iconic_dev.telleriumfarms.Constant
import com.iconic_dev.telleriumfarms.FarmersViewModel
import com.iconic_dev.telleriumfarms.R
import com.iconic_dev.telleriumfarms.databinding.FragmentHomeBinding
import com.iconic_dev.telleriumfarms.db.models.Farmer
import com.iconic_dev.telleriumfarms.farmers.api.FarmersAdapter
import com.iconic_dev.telleriumfarms.ui.maps.MapsActivity
import com.iconic_dev.telleriumfarms.utils.ConfirmDialog
import nl.invissvenska.modalbottomsheetdialog.Item
import nl.invissvenska.modalbottomsheetdialog.ModalBottomSheetDialog
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class HomeFragment : Fragment(), ModalBottomSheetDialog.Listener {

    private lateinit var storedfarmerList: MutableList< Farmer>

    private lateinit var selectedFarmer: Farmer
    private lateinit var bundle: Bundle
    val binding  by  lazy { FragmentHomeBinding.inflate(layoutInflater) }

    private  val REQUEST_PERMISSION_LOCATION = 100
    lateinit var adapter: FarmersAdapter

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


        adapter = FarmersAdapter("", object :   FarmersAdapter.onClickListener{
            override fun viewMore(farmer: Farmer) {
                selectedFarmer = farmer

                confirmPermissions()
            }

        })

        viewModel.listLiveData!!.observe(viewLifecycleOwner, Observer{
            adapter.submitList(it)
        });


        initAdapter()
        setupObservers()
    }


    private fun setupObservers() {
        viewModel.storedFarmers!!.observe(viewLifecycleOwner, Observer{

            storedfarmerList = it
        })





    }



    private fun initAdapter() {
        binding.list.adapter = adapter

        viewModel.listLiveData!!.observe(viewLifecycleOwner, Observer{
            if (it.isNotEmpty()){
                adapter.base_url = it[0]?.base_url?:""
                adapter.submitList(it)
            }

        })

        viewModel.progressLoadStatus.observe(viewLifecycleOwner, Observer{ status ->
            when {
                Objects.requireNonNull(status) ==(Constant.LOADING) -> {
                    binding.progress.visibility = View.VISIBLE
                }
                status ==(Constant.LOADED) -> {
                    binding.progress.visibility = View.GONE
                }
                status== Constant.CHECK_NETWORK_ERROR -> {
                    binding.progress.visibility = View.GONE
                    SweetAlertDialog(context)
                        .setTitleText("Error")
                        .setContentText(Constant.CHECK_NETWORK_ERROR)
                        .show()
                }
            }
        })


    }

    private fun confirmPermissions()  {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( // request permission
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION ),
                REQUEST_PERMISSION_LOCATION
            )
        } else {
            ModalBottomSheetDialog.Builder()
                .setHeader("Farmer Menu")
                .add(R.menu.farmer_options)
                .setRoundedModal(true)
                .show(childFragmentManager, "farmer_menu")
        }
    }

    override fun onItemSelected(tag: String?, item: Item?) {
        bundle = bundleOf("farmer" to selectedFarmer  ,  "base_url" to adapter.base_url)

        val name = "${selectedFarmer.firstName} ${selectedFarmer.surname}"
        when(item!!.id){
           R.id.addLocation->{
               val actions = ConfirmDialog(title = "How do You wish to enter Coordinates", positiveText = "Manual",negativeText = "Map",
                   positiveAction = { showAddCoordinatesManually(name) },
                   negativeAction = {
                       val i = Intent(requireActivity(), MapsActivity::class.java)
                       i.putExtra("farmer",selectedFarmer)
                   }
               )
               actions.show(childFragmentManager, "")
           }


        else-> requireActivity().findNavController(R.id.nav_host).navigate(R.id.updateFarmerFragment, bundle)


        }

    }

    private fun showAddCoordinatesManually(name: String) {
        val flatDialog = FlatDialog(requireActivity())
        flatDialog.setTitle("Add Farm  Coordinates For ${name.toLowerCase().capitalize()}")
            .setBackgroundColor(R.color.colorAccent)
            .setFirstButtonColor(R.color.colorPrimary)
            .setFirstTextFieldHint("Latitude")
            .setSecondTextFieldHint("Longitude")
            .setFirstButtonText("ADD")
            .isCancelable(true)
            .withFirstButtonListner {
                if (flatDialog.firstTextField.isNotEmpty()) {
                    val found = storedfarmerList.any { it.farmerId == selectedFarmer.farmerId }
                    val coordinate = "${flatDialog.firstTextField},${flatDialog.secondTextField}"
                    selectedFarmer.coordinates.add(coordinate)

                    if (found) {
                        val farmerToUpdate =
                            storedfarmerList.filter { it.farmerId == selectedFarmer.farmerId }
                        farmerToUpdate[0].coordinates.add(coordinate)
                        viewModel.updateFarmer(farmerToUpdate[0])
                    } else viewModel.addFarmer(selectedFarmer)
                    flatDialog.dismiss()
                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Complete All Fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .show()
    }


}