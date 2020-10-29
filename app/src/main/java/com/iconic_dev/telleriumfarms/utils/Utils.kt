package com.iconic_dev.telleriumfarms.utils

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.iconic_dev.telleriumfarms.databinding.FragmentLogoutBinding
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.Years
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by manuelchris-ogar on 25/06/2020.
 */

const val DATE_PAYLOAD_FORMAT = "yyyy-MM-dd";
val df: DecimalFormat = DecimalFormat("#.00")
fun TextView.isValid():Boolean{
    return this.text.length > 2
}


fun TextView.isValidName():Boolean{
    return this.text.length > 2
}

fun convertToKg(weight: Double):String{

     return if(weight> 1){
        "$weight Kg"
    }else{
        "$weight g"
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}


fun formatDate(date: String, format: String, newFormat: String):String{

//    val dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
//    val strDateFormat = "yyyy-MM-dd";
//    val todayFormat = "E, MMMM dd"
//    val anotherFormat = "MMMM dd, YYYY"
    val objSDF =  SimpleDateFormat(format);

    try {
        val currentDateTime = objSDF.parse(date)
        val todayBeautifiedFormat = SimpleDateFormat(newFormat)
        return todayBeautifiedFormat.format(currentDateTime)

    }catch (e: Exception){
        Log.e("PPPP", e.localizedMessage)
        return  ""
    }
}


fun getDate(format: String): String {
    var today = DateTime.now().toString()
    val dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    val objSDF =  SimpleDateFormat(dateTimeFormat);
    try {
        val currentDateTime = objSDF.parse(today)
        val currentDateParser = SimpleDateFormat(format)
        today = currentDateParser.format(currentDateTime!!)

    }catch (e: Exception){
        e.printStackTrace()
    }
    return today
}

fun getTodaysDate(): String {
    var today = DateTime.now().toString()
    val dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    val strDateFormat = "yyyy-MM-dd";
    val objSDF =  SimpleDateFormat(dateTimeFormat);
    try {
        val currentDateTime = objSDF.parse(today)
        val currentDateParser = SimpleDateFormat(strDateFormat)
        today = currentDateParser.format(currentDateTime!!)

    }catch (e: Exception){

    }

    return today
}


fun String.toDoubleOrZero():Double{
    return if (this.trim().isEmpty() || this == "." ){
        0.0
    }else{
        this.toDouble()
    }
}

open class OnTextChangedTextWatcher(val functionToExecute: () -> Unit) :  TextWatcher{
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        functionToExecute()
    }

}


fun getFormattedDate(datemillis: Long):String  {
    val anotherFormat = "MMMM dd, YYYY"
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    try {
        val todayBeautifiedFormat = SimpleDateFormat(anotherFormat)
        val todayFormatted = todayBeautifiedFormat.format(datemillis)
//        selectedDate.text = todayFormatted
//        getPickupsForToday(sdf.format(Date(datemillis)))
        return sdf.format(Date(datemillis));

    }catch (e: java.lang.Exception){
        e.printStackTrace()
        return  ""
    }
    return  ""
}


fun getTodaysDateFormatted(format: String):String{
    var today = DateTime.now().toString()
    val dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    val objSDF =  SimpleDateFormat(dateTimeFormat);
    try {
        val currentDateTime = objSDF.parse(today)
        val currentDateParser = SimpleDateFormat(format)
        return currentDateParser.format(currentDateTime!!)

    }catch (e: Exception){

    }

    return ""


}



fun getDateFormatted(format: String, date: String):String{
    var today = DateTime.now().toString()
    val dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    val objSDF =  SimpleDateFormat(dateTimeFormat);
    try {
        val currentDateTime = objSDF.parse(today)
        val currentDateParser = SimpleDateFormat(format)
        return currentDateParser.format(currentDateTime!!)

    }catch (e: Exception){

    }

    return ""


}


fun formatDoubleToTwoDecimalPlaces(number: Number):String{
    return df.format(number)
}


//fun confirmDialog()

class ConfirmDialog(
    val title: String,
    val positiveText: String,
    val negativeText: String,
    val negativeAction: () -> Unit = {},
    val positiveAction: () -> Unit
) : DialogFragment(){

    val binding by lazy { FragmentLogoutBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.title.text = title
        binding.logout.text = positiveText

        binding.goBackHome.text = negativeText

        binding.goBackHome.setOnClickListener {
            dismiss()
            negativeAction()
        }

        binding.logout.setOnClickListener {
            dismiss()
            positiveAction()
        }
    }






}

fun getCurrentDate(): String? {
    val STANDARD_DATE_FORMAT = "yyyy-MM-dd"
    val dateFormatGmt =
        SimpleDateFormat(STANDARD_DATE_FORMAT)
    dateFormatGmt.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormatGmt.format(Date())
}


public fun calculateAge(birthDateList: List<LocalDate>): List<Int> {
    val now = LocalDate()
    return  birthDateList.map { Years.yearsBetween(it, now).years }

}