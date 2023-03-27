package com.fadhil.myfadhilstoryapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "story")
@Parcelize
data class StoryEntity (

    @PrimaryKey
    @field:SerializedName("id")
    var id : String =  "",

    @field:ColumnInfo(name = "name")
    var name : String  = "" ,

    @field:ColumnInfo("photoUrl")
    var photoUrl : String? = null,

    @field:ColumnInfo("description")
    var description : String? = null,

    @field:ColumnInfo("lat")
    var lat : Double? = null,

    @field:ColumnInfo("lon")
    var lon : Double? = null

) : Parcelable