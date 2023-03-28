package com.fadhil.myfadhilstoryapp.start.fragment.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.fadhil.myfadhilstoryapp.R
import com.fadhil.myfadhilstoryapp.databinding.FragmentSplashBinding
import com.fadhil.myfadhilstoryapp.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {


    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSplashBinding.inflate(layoutInflater)
        lifecycleScope.launch {
            delay(delayTime)
            getStatusLogin()

        }

        return binding.root
    }

    private fun getStatusLogin() {
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val isLogin = pref.getBoolean(getString(R.string.isLogin), false)

        if (isLogin) {
            startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }



    companion object {
        const val delayTime = 1500L
    }


}