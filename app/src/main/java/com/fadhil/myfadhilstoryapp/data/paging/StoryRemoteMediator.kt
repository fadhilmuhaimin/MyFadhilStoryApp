package com.fadhil.myfadhilstoryapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity
import com.fadhil.myfadhilstoryapp.data.local.room.RemoteKeys
import com.fadhil.myfadhilstoryapp.data.local.room.StoryDatabase
import com.fadhil.myfadhilstoryapp.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val token : String
) : RemoteMediator<Int, StoryEntity>() {

    private companion object {
        const val INTIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }



    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, StoryEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INTIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        try {
            val resposeData = apiService.getAllStories("bearer $token", 1, page, state.config.pageSize)
            val endOfPaginationReached = resposeData.listStory?.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached!!) null else page.plus(1)
                val keys = resposeData.listStory.map { list ->
                    list?.id?.let { id ->
                        RemoteKeys(id = id, prevKey = prevKey, nextKey = nextKey)
                    }
                }

                database.remoteKeysDao().insertAll(keys as List<RemoteKeys>)
                resposeData.listStory?.let { database.storyDao().insertStory(it) }

            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached!!)
        }catch (e : Exception){
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull  { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { data ->
                database.remoteKeysDao().getRemoteKeysId(data.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

}