package com.fadhil.myfadhilstoryapp.data.remote.retrofit

import com.fadhil.myfadhilstoryapp.data.remote.response.Auth
import com.fadhil.myfadhilstoryapp.data.remote.response.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name : String?,
        @Field("email") email : String?,
        @Field("password") password : String?
    ) : Auth

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email : String?,
        @Field("password") password : String?
    ) : Auth

    @GET("stories")
    suspend fun getAllStories(@Header("Authorization") token: String) : Story


    @Multipart
    @POST("stories")

    suspend fun uploadStory(

        @Header("Authorization") token : String,
        @Part file : MultipartBody.Part,
        @Part("description") description : RequestBody
    ) : Auth

}