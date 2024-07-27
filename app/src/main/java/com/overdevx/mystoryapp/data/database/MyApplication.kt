package com.overdevx.mystoryapp.data.database

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {
    val database by lazy { StoryDatabase.getDatabase(this) }
}