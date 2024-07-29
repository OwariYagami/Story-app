package com.overdevx.mystoryapp.utils

import android.os.Handler
import android.os.Looper
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

class SimpleIdlingResource(private val timeout: Long) : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    private val handler = Handler(Looper.getMainLooper())
    private val countingIdlingResource = CountingIdlingResource("SimpleIdlingResource")

    init {
        handler.postDelayed({
            callback?.onTransitionToIdle()
            countingIdlingResource.decrement()
        }, timeout)
    }

    override fun getName(): String = SimpleIdlingResource::class.java.name

    override fun isIdleNow(): Boolean = countingIdlingResource.isIdleNow

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
        countingIdlingResource.increment()
    }
}
