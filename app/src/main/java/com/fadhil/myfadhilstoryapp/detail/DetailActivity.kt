package com.fadhil.myfadhilstoryapp.detail

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fadhil.myfadhilstoryapp.R
import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity
import com.fadhil.myfadhilstoryapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val data =  intent.getParcelableExtra(KEY_DETAIL,StoryEntity::class.java)

        binding.toolbarDetail.setNavigationOnClickListener {
            finish()
        }
        binding.tvDetailName.text = data?.name
        binding.tvDetailDescription.text = data?.description
        Glide.with(this)
            .load(data?.photoUrl)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
            .into(binding.ivDetailPhoto)
        setContentView(binding.root)
    }

    companion object{
        const val KEY_DETAIL = "Detail"
    }
}