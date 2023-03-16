package com.fadhil.myfadhilstoryapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "story")
class StoryEntity (

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,

    @field:ColumnInfo(name = "name")
    var name : String  = "" ,

    @field:ColumnInfo("photoUrl")
    var photoUrl : String? = null

)