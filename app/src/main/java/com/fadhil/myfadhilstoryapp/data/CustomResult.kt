package com.fadhil.myfadhilstoryapp.data

sealed class CustomResult<out R> private constructor() {
    data class Success<out T>(val data: T) : CustomResult<T>()
    data class Error(val error: String) : CustomResult<Nothing>()
    object Loading : CustomResult<Nothing>()
}