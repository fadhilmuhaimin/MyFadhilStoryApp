package com.fadhil.myfadhilstoryapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity
import com.fadhil.myfadhilstoryapp.data.remote.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(
    private val repository: Repository,
    context: Context
) : ViewModel() {


    var name: String? = null
    var email: String? = null
    var password: String? = null

    var description: String? = null



    val story : LiveData<PagingData<StoryEntity>> =
        repository.getStoryWithMediator(context).cachedIn(viewModelScope)


    fun registerUser() = repository.registerUser(name, email, password)


    fun loginUser() = repository.loginUser(email, password)


    fun uploadStory(
        token: String, file: MultipartBody.Part, desc: RequestBody
    ) = repository.uploadStory(token, file, desc)


}