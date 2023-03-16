package com.fadhil.myfadhilstoryapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity

@Dao
interface StoryDao {

    @Query("SELECT * FROM story" )
    suspend fun getStory():  List<StoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(story: List<StoryEntity?> )

    @Query("DELETE FROM story")
    suspend fun deleteAll()

}