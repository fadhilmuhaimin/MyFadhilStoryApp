package com.fadhil.myfadhilstoryapp.data.paging

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity
import com.fadhil.myfadhilstoryapp.data.local.room.StoryDatabase
import com.fadhil.myfadhilstoryapp.data.remote.response.Auth
import com.fadhil.myfadhilstoryapp.data.remote.response.Story
import com.fadhil.myfadhilstoryapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest{
    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()


    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator =
            StoryRemoteMediator(
                mockDb,
                mockApi,
                "test"

            )

        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }

}

class FakeApiService : ApiService {
    override suspend fun registerUser(name: String?, email: String?, password: String?): Auth {
         return Auth(null,false,"")
    }

    override suspend fun loginUser(email: String?, password: String?): Auth {
        return Auth(null,false,"")

    }


    override suspend fun getAllStories(token: String, location: Int, page: Int, size: Int): Story {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryEntity(
                i.toString(),
                "name + $i",
                "url $i",
            )
            items.add(quote)
        }

        return Story(
            items,
            false,
            "message"
        )
    }

    override suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Auth {
        return Auth(null,false,"")
    }
}