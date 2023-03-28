package com.fadhil.myfadhilstoryapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.fadhil.myfadhilstoryapp.R
import com.fadhil.myfadhilstoryapp.databinding.ActivityProfileBinding
import com.fadhil.myfadhilstoryapp.start.StartActivity

class ProfileActvity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding


    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val name = pref.getString(getString(R.string.name),  "")
        val editor = pref.edit()
        binding.toolbarDetail.setNavigationOnClickListener {
            finish()
        }


        binding.tvName.text = name
        binding.btLogout.setOnClickListener {
            startActivity(Intent(this,StartActivity::class.java))
            finishAffinity()
            editor.clear().apply()
        }
    }
}