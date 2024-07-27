package com.overdevx.mystoryapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(quote: List<ListStoryItemRoom>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryItemRoom>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}