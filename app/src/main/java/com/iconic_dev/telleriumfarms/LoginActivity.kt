package com.iconic_dev.telleriumfarms

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.iconic_dev.telleriumfarms.databinding.ActivityLoginBinding
import com.iconic_dev.telleriumfarms.utils.isValid
import org.koin.android.viewmodel.ext.android.viewModel



class LoginActivity : AppCompatActivity() {

    val viewModel  : FarmersViewModel by viewModel()

    private  val REQUEST_PERMISSION_LOCATION = 100


    val  binding  by lazy { ActivityLoginBinding.inflate(layoutInflater) }


    private var mBackButtonPressedCount = 0
    private var mTimeStamp: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        confirmPermissions()


        if (viewModel.userPrefs.mostRecentUser!= null){
            val prefsUser = viewModel.userPrefs.mostRecentUser
            binding.userEmail.setText(prefsUser.username)
            binding.userPassword.setText(prefsUser.password)
        }


        binding.login.setOnClickListener {
            if (validatePhoneNumber() && validatePin()){
                if (binding.rememberMe.isChecked){
                    viewModel.userPrefs.setMostRecentUser(binding.userEmail.text.toString(), "")
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }



    }

    private fun validatePhoneNumber():Boolean{
        if(!binding.userEmail.isValid()){
            binding.phoneNumberInputLayout.error = "Please input your Phone Number"
            return false
        }
        return if(binding.userEmail.text.toString() != "test@tellerium.io"){
            binding.phoneNumberInputLayout.error = "Incorrect Password"
            false
        }else{
            binding.phoneNumberInputLayout.error = null
            binding.phoneNumberInputLayout.isErrorEnabled = false
            true
        }
    }

    private fun validatePin():Boolean {
        if(binding.userPassword.text.toString() == "password"){
            return true
        }
        return if (!binding.userPassword.isValid()){
            binding.userPasswordInputLayout.error = "Please input your Password"
            false
        } else{
            binding.userPasswordInputLayout.error = "Password Incorrect"
            false
        }
    }


    private fun confirmPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions( // request permission
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION ),
                    REQUEST_PERMISSION_LOCATION
                )
            }
        }
    }


}
