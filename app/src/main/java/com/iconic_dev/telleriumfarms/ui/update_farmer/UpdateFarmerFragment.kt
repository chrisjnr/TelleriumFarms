package com.iconic_dev.telleriumfarms.ui.update_farmer

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.developer.spoti.vspoti.VSpotView
import com.example.flatdialoglibrary.dialog.FlatDialog
import com.iconic_dev.telleriumfarms.FarmersViewModel
import com.iconic_dev.telleriumfarms.R
import com.iconic_dev.telleriumfarms.databinding.FragmentUpdateFarmerBinding
import com.iconic_dev.telleriumfarms.db.models.Farmer
import com.iconic_dev.telleriumfarms.ui.maps.MapsActivity
import com.iconic_dev.telleriumfarms.utils.ConfirmDialog
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class UpdateFarmerFragment : Fragment() {

    private lateinit var farmer: Farmer
    private val  binding by lazy { FragmentUpdateFarmerBinding.inflate(layoutInflater) }

    val viewModel: FarmersViewModel by viewModel()

    private lateinit var base_url:String

    lateinit var adapter : CoordinateAdapter

    private lateinit var storedfarmerList: MutableList<Farmer>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         SweetAlertDialog(context)
            .setTitleText("Info")
            .setContentText(
                "Updating  any Details of the  Farmer\n" +
                        "Automaticaly Saves it locally"
            )
            .show()


        VSpotView.Builder(context)
            .setTitle("Click Here To Access the\nFarmer Menu")
            .setContentText("Options to View The Map\nand Update Farmer Details")
            .setGravity(VSpotView.Gravity.auto) //optional
            .setDismissType(VSpotView.DismissType.outside) //optional - default dismissable by TargetView
            .setTargetView(binding.circleMenu)
            .setContentTextSize(12) //optional
            .setTitleTextSize(14) //optional
            .build()
            .show()


        binding.circleMenu.setOnItemClickListener {
            val name = "${farmer.firstName} ${farmer.surname}"

            try {
                when(it){
                    0 -> {
                        editFarmName()
                    }
                    1 -> {
                        editFarmLocation()
                    }
                    2 -> {
                        val bundle = bundleOf("farmer" to farmer)

                        val actions = ConfirmDialog(title = "How do You wish to enter Coordinates?", positiveText = "Manual",negativeText = "Map",
                            positiveAction = { manuallyAddCoordinates(name)},
                            negativeAction = {
                                val i = Intent(requireActivity(), MapsActivity::class.java)
                                i.putExtra("farmer",farmer)
                                requireActivity().startActivity(i)
                            }
                        )
                        actions.show(childFragmentManager, "")
                    }
                    else->{
                        val  found = storedfarmerList.any { eachFarmer -> eachFarmer.farmerId == farmer.farmerId }

                        if(found) {
                            farmer = storedfarmerList.filter { eachFarmer -> eachFarmer.farmerId == farmer.farmerId }[0]

                        }

                        if(farmer.coordinates.isEmpty()){
                            SweetAlertDialog(context)
                                .setTitleText("Info")
                                .setContentText(
                                    "No Coordinates have been saved yet\n" +
                                            "For this farmer !"
                                )
                                .show()
                        }else{
                            val i = Intent(requireActivity(), MapsActivity::class.java)
                            i.putExtra("farmer",farmer)
                            requireActivity().startActivity(i)
                        }
                    }
                }

            }catch (e:Exception){

            }
        }

        binding.closeFragment.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host).popBackStack()
        }

        arguments?.get("farmer").let {
            try {
                base_url = arguments?.getString("base_url")!!
                farmer = arguments?.get("farmer") as Farmer
                binding.farmerGender.text = farmer.gender
                binding.farmerName.text = "${farmer.firstName.toLowerCase().capitalize()} ${farmer.surname.toLowerCase().capitalize()}"

                val location = if (!farmer.farmLocation.isNullOrBlank()) {
                    farmer.farmLocation
                } else {
                    "Not Yet Set"
                }

                val farmName = if (!farmer.farmName.isNullOrBlank()) {
                    farmer.farmName
                } else {
                    "Not Yet Set"
                }
                binding.farmLocation.text = location


                binding.farmName.text = farmName

                val thumb: String = "${arguments?.getString("base_url")}${farmer.passportPhoto}"

                val placeholder = if (farmer.gender=="Female")  R.drawable.ic_woman else R.drawable.ic_man


                if (!TextUtils.isEmpty(thumb)) Picasso.get().load(thumb)
                    .fit()
                    .centerCrop()
                    .placeholder(placeholder)
                    .into(binding.farmerImage, object : Callback {
                        override fun onSuccess() {
//                            progressBar.setVisibility(View.GONE)
                        }

                        override fun onError(e: Exception) {
//                            progressBar.setVisibility(View.GONE)
                        }
                    }) else {
//                    progressBar.setVisibility(View.GONE)
//                shop_icon.setImageDrawable(app.getDrawable(R.drawable.ee_min))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.storedFarmers!!.observe(viewLifecycleOwner, Observer {


            storedfarmerList = it
            val found =
                storedfarmerList.filter { eachFarmer -> eachFarmer.farmerId == farmer.farmerId }
            adapter = if (found.isNotEmpty()) {
                val location = if (!found[0].farmLocation.isNullOrBlank()) {
                    farmer.farmLocation
                } else {
                    "Not Yet Set"
                }

                val farmName = if (!found[0].farmName.isNullOrBlank()) {
                    farmer.farmName
                } else {
                    "Not Yet Set"
                }
                binding.farmLocation.text = location
                binding.farmName.text = farmName
                CoordinateAdapter(found[0].coordinates)
            } else CoordinateAdapter(ArrayList())
            if (adapter.coordinatesList.isNotEmpty()) binding.emptyList.visibility = View.GONE

            binding.updateFarmerRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.updateFarmerRecyclerView.adapter = adapter
        })








    }

    private fun manuallyAddCoordinates(name: String) {
        val flatDialog = FlatDialog(requireActivity())
        flatDialog.setTitle(
            "Add Farm  Coordinates For ${
                name.toLowerCase().capitalize()
            }"
        )
            .setFirstTextFieldHint("Latitude")
            .setSecondTextFieldHint("Longitude")
            .setFirstButtonText("ADD")
            .isCancelable(true)
            .withFirstButtonListner {
                if (flatDialog.firstTextField.isNotEmpty()) {
                    val found =
                        storedfarmerList.any { eachFarmer -> eachFarmer.farmerId == farmer.farmerId }
                    val coordinate =
                        "${flatDialog.firstTextField},${flatDialog.secondTextField}"
                    farmer.coordinates.add(coordinate)

                    if (found) {
                        val farmerToUpdate =
                            storedfarmerList.filter { eachFarmer -> eachFarmer.farmerId == farmer.farmerId }
                        farmerToUpdate[0].coordinates.add(coordinate)
                        viewModel.updateFarmer(farmerToUpdate[0])
                    } else viewModel.addFarmer(farmer)
                    flatDialog.dismiss()
                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Complete All Fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .show()
    }


    private fun editFarmName(){
        val edittext = EditText(context)
        edittext.background.mutate().setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.design_default_color_on_primary
            ), PorterDuff.Mode.SRC_ATOP
        )
        val linearLayout = LinearLayout(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        edittext.hint = "Type Farm Name"
        linearLayout.setPadding(16, 0, 16, 0)
        linearLayout.addView(edittext)



        val alert = AlertDialog.Builder(requireContext())
        alert.setView(linearLayout)
        alert.setCancelable(false)

        alert.setPositiveButton("SAVE", null)

        alert.setNegativeButton("CANCEL", null)

        val dialog = alert.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            positiveButton.setOnClickListener {
                if (edittext.text.toString().isNotEmpty()) {
                    val  found = storedfarmerList.any { it.farmerId == farmer.farmerId }

                    farmer.farmName  = edittext.text.toString()
                    farmer.passportPhoto = "$base_url${farmer.passportPhoto}"
                    if(found) {
                        val farmerToUpdate = storedfarmerList.filter { it.farmerId == farmer.farmerId }
                        farmerToUpdate[0].farmName = edittext.text.toString()
                        viewModel.updateFarmer(farmerToUpdate[0])
                    }
                    else viewModel.addFarmer(farmer)

                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Complete All Fields", Toast.LENGTH_SHORT).show()

                }
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
        dialog.show()
    }




    private fun editFarmLocation(){
        val edittext = EditText(context)
        edittext.background.mutate().setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.design_default_color_on_primary
            ), PorterDuff.Mode.SRC_ATOP
        )
        val linearLayout = LinearLayout(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        edittext.hint = "Type Farm Location"
        linearLayout.setPadding(16, 0, 16, 0)
        linearLayout.addView(edittext)



        val alert = AlertDialog.Builder(requireContext())
        alert.setView(linearLayout)
        alert.setCancelable(false)

        alert.setPositiveButton("SAVE", null)

        alert.setNegativeButton("CANCEL", null)

        val dialog = alert.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            positiveButton.setOnClickListener {
                if (edittext.text.toString().isNotEmpty()) {
                    val  found = storedfarmerList.any { it.farmerId == farmer.farmerId }

                    farmer.farmLocation  = edittext.text.toString()
                    if(found) {
                        val farmerToUpdate = storedfarmerList.filter { it.farmerId == farmer.farmerId }
                        farmerToUpdate[0].farmLocation = edittext.text.toString()
                        viewModel.updateFarmer(farmerToUpdate[0])
                    }
                    else viewModel.addFarmer(farmer)

                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Complete All Fields", Toast.LENGTH_SHORT).show()

                }
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
        dialog.show()
    }

    fun showEditTitleDialog(){
        val edittext = EditText(context)
        edittext.background.mutate().setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.design_default_color_on_primary
            ), PorterDuff.Mode.SRC_ATOP
        )
        val linearLayout = LinearLayout(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
//        edittext.layoutParams = lp
//        linearLayout.setPadding(16, 0, 16, 0)
        linearLayout.addView(edittext)
        edittext.hint = "Type Longitude..."



        val edittextLatitude = EditText(context)
        edittextLatitude.background.mutate().setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.design_default_color_on_primary
            ), PorterDuff.Mode.SRC_ATOP
        )
//        edittextLatitude.layoutParams = lp
//        linearLayout.setPadding(16, 0, 16, 0)
        linearLayout.addView(edittextLatitude)
        edittextLatitude.hint = "Type Latitude..."


        val alert = AlertDialog.Builder(requireContext())
        alert.setMessage("Add Farm Coordinates")
        alert.setView(linearLayout)
        alert.setCancelable(false)

        alert.setPositiveButton("SAVE", null)

        alert.setNegativeButton("CANCEL", null)

        val dialog = alert.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            positiveButton.setOnClickListener {
                if (edittext.text.toString().isNotEmpty()) {
                    val  found = storedfarmerList.any { it.farmerId == farmer.farmerId }
                    val coordinate = "${edittext.text},${edittextLatitude.text}"
                    farmer.coordinates.add(coordinate)

                    if(found) {
                        val farmerToUpdate = storedfarmerList.filter { it.farmerId == farmer.farmerId }
                        farmerToUpdate[0].coordinates.add(coordinate)
                        viewModel.updateFarmer(farmerToUpdate[0])
                    }
                    else viewModel.addFarmer(farmer)

                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Complete All Fields", Toast.LENGTH_SHORT).show()

                }
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
        dialog.show()
    }

    override fun onStop() {
        super.onStop()
        binding.circleMenu.visibility== View.GONE
    }

    override fun onResume() {
        super.onResume()
        binding.circleMenu.visibility== View.VISIBLE


    }


}