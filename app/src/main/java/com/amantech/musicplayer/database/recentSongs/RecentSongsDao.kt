package com.amantech.musicplayer.database.recentSongs

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface RecentSongsDao {
    //Inserting song to recents
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentSong(recentSongEntity: RecentSongEntity)

    // Deleting a song in case
    @Delete
    suspend fun removeRecentSong(recentSongEntity: RecentSongEntity)

    //Retrieve all recent songs in order from the database
    @get:Query("SELECT * from recent_songs_table order by lastPlayed desc")
    val allSongs: LiveData<List<RecentSongEntity>>


}
