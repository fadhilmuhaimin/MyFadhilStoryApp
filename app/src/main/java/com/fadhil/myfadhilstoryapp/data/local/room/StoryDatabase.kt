package com.fadhil.myfadhilstoryapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity


@Database(entities = [StoryEntity::class, RemoteKeys::class], version = 4, exportSchema = false)
abstract class StoryDatabase  : RoomDatabase() {
    abstract fun storyDao() : StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object{
        @Volatile
        private var instance : StoryDatabase? = null
        fun getInstance(context: Context) : StoryDatabase{
            return instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "story_database"

                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }

    }
}