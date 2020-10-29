package com.iconic_dev.telleriumfarms.ui.dash

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.use
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.faskn.lib.Slice
import com.faskn.lib.buildChart
import com.iconic_dev.telleriumfarms.FarmersViewModel
import com.iconic_dev.telleriumfarms.R
import com.iconic_dev.telleriumfarms.databinding.FragmentDashboardBinding
import org.joda.time.LocalDate
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception
import kotlin.random.Random


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


            val  allFarmers = it.filterNotNull()


            val birthDateList =  it.map { farmer -> LocalDate(farmer.dob.split("-")[0].toInt(), farmer.dob.split("-")[1].toInt(),farmer.dob.split("-")[2].toInt()) }


            val age =  com.iconic_dev.telleriumfarms.utils.calculateAge(birthDateList)


            val less30 = age.filter { eachAge -> eachAge<30 }

            val less30to40 = age.filter { eachAge -> eachAge<=30 && eachAge<=40 }

            val less40to50 = age.filter { eachAge -> eachAge<=40 && eachAge<=50 }

            val less50to60 = age.filter { eachAge -> eachAge<=50 && eachAge<=60 }

            val sixtyplus = age.filter { eachAge -> eachAge>60 }

            val farmersGender = it.map { eachFarmer -> eachFarmer.gender }

            val women = farmersGender.filter { gender ->gender  == "Female" }
            val men = farmersGender.size - women.size


            val listOfStates = it.map { farmer ->farmer.state }.toSet()

            val hashMap  = HashMap<String, Int>()
            listOfStates.forEach {
                stateName->
                hashMap[stateName] = 0
            }

            it.forEach {
                farmer->
                run {
                    if (hashMap.containsKey(farmer.state)) {
                        val number = hashMap[farmer.state]!!.toInt()
                        hashMap[farmer.state] = number + 1
                    }
                }
            }


            val colors = resources.obtainTypedArray(R.array.pie_colors)
            val charDataForStates = hashMap.map { hash-> Slice(hash.value.toFloat(), R.color.main_blue_stroke_color,  hash.key)}.toMutableList()
            val pieStates  = java.util.ArrayList<Slice>()
            pieStates.addAll(charDataForStates)

            binding.statsText.text = "There are Currently ${women.size} Women and $men Men whose details have been  updated and Stored"

            val pieAges  = arrayListOf(Slice(
                women.size.toFloat(),
                R.color.colorPrimary,
                "Female"
            ),(Slice(
                men.toFloat(),
                R.color.colorAccent,
                "Male"
            )
            ))


            binding.numberOfFarms.text = "Number of Registered Farms : ${it.filter { farmer -> !farmer.farmName.isNullOrBlank() }.size.toString()}"



            binding.anyChartViewBar.setProgressBar(binding.progressBar1)

            val cartesian = AnyChart.column()

            val chartdata: MutableList<DataEntry> = ArrayList()

            chartdata.add(ValueDataEntry("<30", less30.size))
            chartdata.add(ValueDataEntry("30-40", less30to40.size))
            chartdata.add(ValueDataEntry("40-50", less40to50.size))
            chartdata.add(ValueDataEntry("50-60", less50to60.size))
            chartdata.add(ValueDataEntry("60+", sixtyplus.size))


            val column= cartesian.column(chartdata)

            column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                .format("\${%Value}{groupsSeparator: }")

            cartesian.animation(true)
            cartesian.title("Chart for Age Ranges")

            cartesian.yScale().minimum(0.0)


            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)

            cartesian.xAxis(0).title("Age")
            cartesian.yAxis(0).title("Number")

            binding.anyChartViewBar.setChart(cartesian)


            try {
                val pieChartDSL = buildChart {
                    slices { pieAges }
                    sliceWidth { 50f }
                    sliceStartPoint { 0f }
                    clickListener { angle, index ->
                        // ...
                    }
                }

                binding.chart.setPieChart(pieChartDSL)
                binding.chart.showLegend(binding.legendLayout)
            }catch (e:Exception){
                e.printStackTrace()
            }




            try {
                val pieChartStates = buildChart {
                    slices { pieStates }
                    sliceWidth { 50f }
                    sliceStartPoint { 0f }
                    clickListener { angle, index ->
                        // ...
                    }
                }

                binding.chartStates.setPieChart(pieChartStates)
                binding.chartStates.showLegend(binding.legendLayoutStates)
            }catch (e:Exception){
                e.printStackTrace()
            }





        })







    }
}