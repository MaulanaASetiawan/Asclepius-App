package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.Bookmarks

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM Bookmarks ORDER BY id ASC")
    fun getAllBookmark(): LiveData<List<Bookmarks>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookmark(bookmarks: Bookmarks)
}