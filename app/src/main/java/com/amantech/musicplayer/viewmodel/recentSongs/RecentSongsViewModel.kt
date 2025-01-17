package com.amantech.musicplayer.viewmodel.recentSongs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amantech.musicplayer.database.recentSongs.RecentSongEntity
import com.amantech.musicplayer.repository.RecentSongsRepository
import kotlinx.coroutines.launch

class RecentSongsViewModel(application: Application) : ViewModel() {
    private val mRecentSongsRepository: RecentSongsRepository = RecentSongsRepository(application)
    val recentSongs: LiveData<List<RecentSongEntity>>
        get() = mRecentSongsRepository.mRecentSongs


    fun deleteRecentSong(song: RecentSongEntity) {
        //use of coroutine scope from viewModelScope
        viewModelScope.launch {
            mRecentSongsRepository.deleteSong(song)
        }
    }

    fun updateRecentSong(song: RecentSongEntity) {
        //use of coroutine scope from viewModelScope
        viewModelScope.launch {
            mRecentSongsRepository.insertSong(song)
        }
    }
}