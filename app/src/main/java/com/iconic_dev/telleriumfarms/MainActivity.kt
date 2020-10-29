package com.iconic_dev.telleriumfarms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.iconic_dev.telleriumfarms.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)  }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host) as NavHostFragment?
            NavigationUI.setupWithNavController(binding.navBar, navHostFragment!!.findNavController())

            binding.navBar.itemIconTintList = null




    }
}