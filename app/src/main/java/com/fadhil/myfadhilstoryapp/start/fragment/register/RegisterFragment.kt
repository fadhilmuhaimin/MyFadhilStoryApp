package com.fadhil.myfadhilstoryapp.start.fragment.register

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fadhil.myfadhilstoryapp.R
import com.fadhil.myfadhilstoryapp.StoryViewModel
import com.fadhil.myfadhilstoryapp.ViewModelFactory
import com.fadhil.myfadhilstoryapp.data.CustomResult
import com.fadhil.myfadhilstoryapp.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private val viewModel by viewModels<StoryViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var binding : FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.btRegister.setOnClickListener {
            registerUser()

        }


        return binding.root
    }

    private fun correctInput(): Boolean = with(binding) {
        var isCorrect = true
        clean()
        viewModel.name = edRegisterName.text.toString()
        viewModel.email = edRegisterEmail.text.toString()
        viewModel.password = edRegisterPassword.text.toString()

        if (viewModel.name.isNullOrEmpty()){
            edRegisterName.error = getString(R.string.error_name)
            isCorrect = false
        }
        if (viewModel.email.isNullOrEmpty()){
            edRegisterEmail.error = getString(R.string.error_email)
            isCorrect = false
        }
        if (viewModel.password.isNullOrEmpty()){
            edRegisterPassword.error = getString(R.string.error_password)
            isCorrect = false
        }

        return isCorrect

    }

    private fun clean() = with(binding){
        edRegisterName.error = null
        edRegisterEmail.error = null
        edRegisterPassword.error = null

    }

    private fun registerUser() {
        if (correctInput()){
            viewModel.registerUser().observe(viewLifecycleOwner){ data ->
                when(data){
                    is CustomResult.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                    }

                    is CustomResult.Error -> {
                        binding.progress.visibility = View.GONE
                        Log.d(TAG, "registerUser: ${data.error} ")
                        Toast.makeText(context, "Gagal Karena ${data.error}", Toast.LENGTH_SHORT).show()

                    }
                    is CustomResult.Success -> {
                        binding.progress.visibility = View.VISIBLE
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                }

            }

        }else{
            binding.btRegister.isEnabled = false
        }
    }


}