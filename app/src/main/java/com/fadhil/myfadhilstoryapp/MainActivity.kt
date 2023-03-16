package com.fadhil.myfadhilstoryapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.fadhil.myfadhilstoryapp.data.CustomResult
import com.fadhil.myfadhilstoryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)





        val storyAdapter = StoryAdapter()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(getString(R.string.token), "")

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this,AddActivity::class.java))
        }


        token?.let { token ->
            viewModel.getAllStory("bearer $token").observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is CustomResult.Loading -> {
                            binding.progress.visibility = View.VISIBLE

                        }
                        is CustomResult.Error -> {
                            binding.progress.visibility = View.GONE
                            binding.ivNodata.visibility = View.VISIBLE
                            binding.tvNodata.visibility = View.VISIBLE

                            Log.e(TAG, "onCreates: ${result.error}  apa $token")
                            Toast.makeText(this, "Gagal Memuat Data", Toast.LENGTH_SHORT).show()

                        }
                        is CustomResult.Success -> {
                            binding.progress.visibility = View.GONE
                            storyAdapter.submitList(result.data)
                        }
                    }
                }

            }
        }
        binding.rvStory.apply {
            layoutManager = GridLayoutManager(this@MainActivity,2)
            setHasFixedSize(true)
            adapter = storyAdapter
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_appbar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            R.id.nav_profile -> {
                startActivity(Intent(this,ProfileActvity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }



}