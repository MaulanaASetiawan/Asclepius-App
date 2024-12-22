package com.dicoding.asclepius.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "bookmarks")
data class Bookmarks (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var label: String? = null,
    var confidence: Float? = null,
    var image: String? = null
) : Parcelable