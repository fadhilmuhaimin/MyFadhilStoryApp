package com.fadhil.myfadhilstoryapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.fadhil.myfadhilstoryapp.R
import com.fadhil.myfadhilstoryapp.data.local.entity.StoryEntity
import com.fadhil.myfadhilstoryapp.data.local.room.StoryDao
import com.fadhil.myfadhilstoryapp.data.local.room.StoryDatabase
import kotlinx.coroutines.runBlocking


internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems = listOf<StoryEntity>()
    private lateinit var dao : StoryDao

    override fun onCreate() {
        dao = StoryDatabase.getInstance(mContext.applicationContext).storyDao()
        getData()

    }

    private fun getData() {
         runBlocking {
             mWidgetItems = dao.getStory()
         }
    }

    override fun onDataSetChanged() {
        getData()
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        val bitmap: Bitmap = Glide.with(mContext.applicationContext)
            .asBitmap()
            .load(mWidgetItems[position].photoUrl)
            .submit()
            .get()

        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val extras = bundleOf(
                ImagesBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}