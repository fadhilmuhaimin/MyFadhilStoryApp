package com.fadhil.myfadhilstoryapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.fadhil.myfadhilstoryapp.R
import com.fadhil.myfadhilstoryapp.StoryViewModel
import com.fadhil.myfadhilstoryapp.ViewModelFactory
import com.fadhil.myfadhilstoryapp.adapter.LoadingStateAdapter
import com.fadhil.myfadhilstoryapp.adapter.StoryAdapter
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
        getData()


        binding.fabAdd.setOnClickListener {

            startActivity(Intent(this, AddActivity::class.java))
        }


    }

    private fun getData() {
        val storyAdapter = StoryAdapter()

        binding.rvStory.apply {
            layoutManager = GridLayoutManager(this@MainActivity,2)
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    storyAdapter.retry()
                }
            )
        }
        viewModel.story.observe(this){
            storyAdapter.submitData(lifecycle,it)
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
                startActivity(Intent(this, ProfileActvity::class.java))
            }
            R.id.nav_map -> {
                startActivity(Intent(this,MapsActivity::class.java))


            }


        }
        return super.onOptionsItemSelected(item)
    }



}