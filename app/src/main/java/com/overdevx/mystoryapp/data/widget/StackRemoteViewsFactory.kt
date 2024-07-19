package com.overdevx.mystoryapp.data.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.net.URL

internal class StackRemoteViewsFactory(private val mContext: Context, private val userRepository: UserRepository) : RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<Bitmap>()
    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        mWidgetItems.clear()
        runBlocking {
            val token = userRepository.getToken().first() ?: ""
            val apiService = ApiConfig.getApiServicesWithToken(token)
            try {
                val response = userRepository.getListStory(1, 10)
                if (response.error==false) {
                    val stories = response.listStory ?: emptyList()
                    stories.forEach { story ->
                        val imageUrl = story?.photoUrl
                        val bitmap = imageUrl?.let { getBitmapFromUrl(it) }
                        if (bitmap != null) {
                            mWidgetItems.add(bitmap)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.img))
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.pp_user))
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.img_slide_1))
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.img))
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.img))

    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])
        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
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

    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}