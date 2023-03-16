package com.fadhil.myfadhilstoryapp

import androidx.lifecycle.ViewModel
import com.fadhil.myfadhilstoryapp.data.remote.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val repository: Repository) : ViewModel() {


    var name : String? = null
    var email : String? = null
    var password : String? = null

    var description : String? = null


    fun registerUser() = repository.registerUser(name,email,password)


    fun loginUser() = repository.loginUser(email,password)

    fun getAllStory(token : String) = repository.getAllStories(token)

    fun uploadStory(
                    token: String
                    ,file : MultipartBody.Part
                    ,desc : RequestBody) = repository.uploadStory( token,file,desc)


}