package com.iconic_dev.telleriumfarms.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.iconic_dev.telleriumfarms.LoginActivity
import com.iconic_dev.telleriumfarms.R
import com.iconic_dev.telleriumfarms.databinding.FragmentLogoutBinding


class LogoutFragment : DialogFragment() {

    val binding by lazy { FragmentLogoutBinding.inflate(layoutInflater) }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goBackHome.setOnClickListener {
            dismiss()
        }


        binding.logout.setOnClickListener {
            val i = Intent(requireActivity(), LoginActivity::class.java)
            requireActivity().startActivity(i)
            requireActivity().finish()

        }


    }
}