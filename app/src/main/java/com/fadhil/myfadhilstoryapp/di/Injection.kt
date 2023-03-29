package com.fadhil.myfadhilstoryapp.di

import android.content.Context
import com.fadhil.myfadhilstoryapp.data.local.room.StoryDatabase
import com.fadhil.myfadhilstoryapp.data.remote.Repository
import com.fadhil.myfadhilstoryapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        return Repository.getInstance(apiService,database)
    }
}