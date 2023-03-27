package com.fadhil.myfadhilstoryapp.start.fragment.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.fadhil.myfadhilstoryapp.ui.MainActivity
import com.fadhil.myfadhilstoryapp.R
import com.fadhil.myfadhilstoryapp.StoryViewModel
import com.fadhil.myfadhilstoryapp.ViewModelFactory
import com.fadhil.myfadhilstoryapp.data.CustomResult
import com.fadhil.myfadhilstoryapp.data.remote.response.LoginResult
import com.fadhil.myfadhilstoryapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var  binding: FragmentLoginBinding
    private val viewModel by viewModels<StoryViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.btLogin.setOnClickListener {

            loginUser()
        }

        binding.tvDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    private fun loginUser() {

        if (correctInput()){
            viewModel.loginUser().observe(viewLifecycleOwner){ data ->
                when(data){
                    is CustomResult.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                    }

                    is CustomResult.Error -> {
                        binding.progress.visibility = View.GONE
                        Log.d(ContentValues.TAG, "registerUser: ${data.error} ")
                        Toast.makeText(context, "Gagal Karena ${data.error}", Toast.LENGTH_SHORT).show()

                    }
                    is CustomResult.Success -> {
                        binding.progress.visibility = View.VISIBLE
                        startActivity(Intent(context, MainActivity::class.java))
                        activity?.finish()
                        saveUserLoginState(true,data.data.loginResult)
                    }
                }

            }

        }else{
            binding.btLogin.isEnabled = false
        }
    }

    private fun correctInput(): Boolean = with(binding) {
        var isCorrect = true
        clean()

        viewModel.email = edLoginEmail.text.toString()
        viewModel.password = edLoginPassword.text.toString()


        if (viewModel.email.isNullOrEmpty()){
            edLoginEmail.error = getString(R.string.error_email)
            isCorrect = false
        }
        if (viewModel.password.isNullOrEmpty()){
            edLoginPassword.error = getString(R.string.error_password)
            isCorrect = false
        }

        return isCorrect

    }

    private fun clean() = with(binding){
        edLoginEmail.error = null
        edLoginPassword.error = null
    }

    private fun saveUserLoginState(status: Boolean, loginResult: LoginResult?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = pref.edit()
        editor.putBoolean(getString(R.string.isLogin), status)
        editor.putString(getString(R.string.token),loginResult?.token)
        editor.putString(getString(R.string.name),loginResult?.name)
        editor.apply()
    }


}