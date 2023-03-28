package com.fadhil.myfadhilstoryapp.data.remote


import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.*
import androidx.preference.PreferenceManager
import com.fadhil.myfadhilstoryapp.R
import com.fadhil.myfadhilstoryapp.data.CustomResult
import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity
import com.fadhil.myfadhilstoryapp.data.local.room.StoryDatabase
import com.fadhil.myfadhilstoryapp.data.paging.StoryRemoteMediator
import com.fadhil.myfadhilstoryapp.data.remote.response.Auth
import com.fadhil.myfadhilstoryapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody


class Repository private constructor(
        private val apiService: ApiService,
        private val storyDatabase: StoryDatabase
){
    private val _responseRegister = MutableLiveData<Auth>()
    private val responseRegister : LiveData<Auth> =_responseRegister

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


    fun getStoryWithMediator(context: Context) : LiveData<PagingData<StoryEntity>>{

        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val token = pref.getString(context.getString(R.string.token), "")
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = token?.let {
                StoryRemoteMediator(database = storyDatabase,apiService,
                    it
                )
            },
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }



    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            storyDatabase: StoryDatabase
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService,storyDatabase)
            }.also { instance = it }
    }


}