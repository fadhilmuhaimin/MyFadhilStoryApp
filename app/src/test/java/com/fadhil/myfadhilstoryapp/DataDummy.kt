package com.fadhil.myfadhilstoryapp

import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity


object DataDummy {

    fun generateDummyQuoteResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryEntity(
                i.toString(),
                "fadhil + $i",
                "test $i",
            )
            items.add(quote)
        }
        return items
    }
}