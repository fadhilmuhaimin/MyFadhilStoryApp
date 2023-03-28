package com.fadhil.myfadhilstoryapp.data.remote.response

import android.os.Parcelable
import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity
import kotlinx.android.parcel.Parcelize

data class Story(
	val listStory: List<StoryEntity?>? = null,
	val error: Boolean? = null,
	val message: String? = null
)
@Parcelize
data class ListStoryItem(
	val photoUrl: String? = null,
	val createdAt: String? = null,
	val name: String? = null,
	val description: String? = null,
	val id: String? = null,
	val lat : Double? = null,
	val lon : Double? = null
) : Parcelable

