package com.amantech.musicplayer.viewmodel.allSongs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amantech.musicplayer.database.allSongs.SongEntity
import com.amantech.musicplayer.repository.AllSongsRepository
import kotlinx.coroutines.launch


class AllSongsViewModel(application: Application) : ViewModel() {


    private val mAllSongsRepository: AllSongsRepository = AllSongsRepository(application)
    val allSongs: LiveData<List<SongEntity>>
        get() = mAllSongsRepository.mAllSongs

    val favSongs: LiveData<List<SongEntity>>
        get() = mAllSongsRepository.mFavSongs

    fun insertSongs(songsList: List<SongEntity>) {
        //use of coroutine scope from viewModelScope
        viewModelScope.launch {
            mAllSongsRepository.insertSongs(songsList)
        }
    }

    fun insertSong(song: SongEntity) {
        //use of coroutine scope from viewModelScope
        viewModelScope.launch {
            mAllSongsRepository.insertSong(song)
        }
    }

    suspend fun getAllSongs():List<SongEntity>{
        return mAllSongsRepository.getAllSongs()
    }


    fun removeSong(songEntity: SongEntity) {
        //use of coroutine scope from viewModelScope
        viewModelScope.launch {
            mAllSongsRepository.removeSong(songEntity)
        }
    }

    suspend fun getSongByIdSuspend(id: Int): SongEntity {
        return mAllSongsRepository.getSongById(id)
    }

    fun getSongById(id: Int): SongEntity {
        var mSongEntity: SongEntity? = null

        viewModelScope.launch {
            mSongEntity = mAllSongsRepository.getSongById(id)
        }
        return mSongEntity!!
    }

    fun checkFav(id: Int): Int {
        var isFav = -1
        viewModelScope.launch {
            isFav = mAllSongsRepository.checkFav(id)
        }
        return isFav
    }

    fun updateFav(id: Int) {
        viewModelScope.launch {
            mAllSongsRepository.updateFav(id)
        }
    }
}