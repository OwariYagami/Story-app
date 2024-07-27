package com.overdevx.mystoryapp.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.overdevx.mystoryapp.data.response.ListStoryItem
import kotlinx.parcelize.Parcelize

data class ResponseListStoryRoom(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItemRoom?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
@Entity(tableName = "story")
@Parcelize
data class ListStoryItemRoom(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lat")
	val lat: Double? = null
) : Parcelable

fun ListStoryItem.toRoomEntity(): ListStoryItemRoom {
	return ListStoryItemRoom(
		id = this.id ?: "",
		photoUrl = this.photoUrl,
		createdAt = this.createdAt,
		name = this.name,
		description = this.description,
		lon = this.lon,
		lat = this.lat
	)
}