package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.Bookmarks

@Database(entities = [Bookmarks::class], version = 4, exportSchema = false)
abstract class BookmarkDb: RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var instance: BookmarkDb? = null
        fun getInstance(context: Context): BookmarkDb =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDb::class.java, "Bookmark.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
    }
}