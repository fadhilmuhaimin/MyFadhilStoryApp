package com.fadhil.myfadhilstoryapp.data.remote

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.fadhil.myfadhilstoryapp.data.CustomResult
import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity
import com.fadhil.myfadhilstoryapp.data.local.room.StoryDao
import com.fadhil.myfadhilstoryapp.data.remote.response.Auth
import com.fadhil.myfadhilstoryapp.data.remote.response.ListStoryItem
import com.fadhil.myfadhilstoryapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody


class Repository private constructor(
        private val apiService: ApiService,
        private val storyDao: StoryDao
){
    private val _responseRegister = MutableLiveData<Auth>()
    private val responseRegister : LiveData<Auth> =_responseRegister

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    private val listStory : LiveData<List<ListStoryItem>> = _listStory

    private val _responseUpload = MutableLiveData<Auth>()
    private val responseUpload : LiveData<Auth> =_responseUpload

    fun uploadStory(  token : String, file : MultipartBody.Part, desc : RequestBody)= liveData  {
        emit(CustomResult.Loading)
        try {
            val response = apiService.uploadStory( token,file,desc)

            _responseUpload.value = response

        }catch (e : Exception){
            emit(CustomResult.Error(e.message.toString()))
            Log.e(TAG, "uploadStory: , ${e.message}")
        }

        val data : LiveData<CustomResult<Auth>> = responseUpload.map { CustomResult.Success(it) }
        emitSource(data)
    }



    fun registerUser(name : String?, email : String?, password : String? )  = liveData {
        emit(CustomResult.Loading)
        try {
            val response = apiService.registerUser(name, email,password)
            _responseRegister.value = response
        }catch (e : Exception){
            emit(CustomResult.Error(e.message.toString()))
        }
        val data : LiveData<CustomResult<Auth>> = responseRegister.map { CustomResult.Success(it) }
        emitSource(data)
    }




    fun loginUser(email : String?, password : String? )  = liveData {
        emit(CustomResult.Loading)
        try {
            val response = apiService.loginUser(email,password)
            _responseRegister.value = response
        }catch (e : Exception){
            emit(CustomResult.Error(e.message.toString()))
        }
        val data : LiveData<CustomResult<Auth>> = responseRegister.map { CustomResult.Success(it) }
        emitSource(data)
    }

    fun getAllStories(token : String) : LiveData<CustomResult<List<ListStoryItem>>> = liveData {
        emit(CustomResult.Loading)
        try {
            val response = apiService.getAllStories(token)
            _listStory.value = response.listStory as List<ListStoryItem>?
            val listWidget = response.listStory?.map { list ->
                list.name?.let { name ->
                    StoryEntity(
                        0,
                        name,
                        list.photoUrl
                    )
                }

            }
            storyDao.deleteAll()
            listWidget?.let { storyDao.insertStory(it) }

        }catch (e : Exception){
            emit(CustomResult.Error(e.message.toString()))
        }
        val data : LiveData<CustomResult<List<ListStoryItem>>> = listStory.map { CustomResult.Success(it) }
        emitSource(data)

    }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            storyDao : StoryDao
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService,storyDao)
            }.also { instance = it }
    }


}