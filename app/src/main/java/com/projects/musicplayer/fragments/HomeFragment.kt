package com.projects.musicplayer.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projects.musicplayer.adapters.AllSongsAapter
import com.projects.musicplayer.R
import com.projects.musicplayer.adapters.PlaylistDialogAdapter
import com.projects.musicplayer.adapters.RecentTracksAdapter
import com.projects.musicplayer.database.PlaylistEntity
import com.projects.musicplayer.database.RecentSongEntity
import com.projects.musicplayer.uicomponents.CustomDialog
import com.projects.musicplayer.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeFragment : Fragment() {

    lateinit var recyclerViewAllSongs: RecyclerView
    lateinit var adapterAllSongs: AllSongsAapter

    lateinit var recyclerViewRecentTracks: RecyclerView
    lateinit var adapterRecentTracks: RecentTracksAdapter

    //view model related
    private lateinit var mAllSongsViewModel: AllSongsViewModel
    private lateinit var mAllSongsViewModelFactory: AllSongsViewModelFactory
    private lateinit var mRecentSongsViewModel: RecentSongsViewModel
    private lateinit var mRecentSongsViewModelFactory: RecentSongsViewModelFactory

    private lateinit var mPlaylistViewModel: PlaylistViewModel
    private lateinit var mPlaylistViewModelFactory: PlaylistViewModelFactory

    lateinit var createPlaylistDialog: AddToPlaylist
    lateinit var playlistInputDialog: CustomDialog

    private val uiscope = CoroutineScope(Dispatchers.Main)

    var selectedSongId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAllSongsViewModelFactory = AllSongsViewModelFactory(activity!!.application)
        mAllSongsViewModel =
            ViewModelProvider(this, mAllSongsViewModelFactory).get(AllSongsViewModel::class.java)

        mAllSongsViewModel.allSongs.observe(viewLifecycleOwner, Observer {
            Log.i("LIVEDATA-UPDATE", "Setting all songs again")
            adapterAllSongs.setSongs(it!!)
        })

        adapterAllSongs.favClickCallback = fun(id: Int) {
            //update fav whenever fav button clicked
            uiscope.launch {
                mAllSongsViewModel.updateFav(id)
            }
        }

        mPlaylistViewModelFactory = PlaylistViewModelFactory(activity!!.application)
        mPlaylistViewModel =
            ViewModelProvider(this, mPlaylistViewModelFactory).get(PlaylistViewModel::class.java)
        mPlaylistViewModel.allPlaylists.observe(viewLifecycleOwner, Observer {
            createPlaylistDialog.setDialogPlaylists(it!!)
        })


        mRecentSongsViewModelFactory = RecentSongsViewModelFactory(activity!!.application)
        mRecentSongsViewModel =
            ViewModelProvider(
                this,
                mRecentSongsViewModelFactory
            ).get(RecentSongsViewModel::class.java)

        mRecentSongsViewModel.recentSongs.observe(viewLifecycleOwner, Observer {
            Log.i("LIVEDATA-UPDATE", "Setting recent songs again")//TODO continue
            adapterRecentTracks.addTracks(it!!)
        })

        adapterAllSongs.onSongClickCallback = fun(song: RecentSongEntity) {
            //update recent tracks
            uiscope.launch {
                mRecentSongsViewModel.insertAfterDeleteSong(song)
            }
        }

        adapterRecentTracks.onSongClickCallback = fun(song: RecentSongEntity) {
            //update recent tracks
            uiscope.launch {
                mRecentSongsViewModel.insertAfterDeleteSong(song)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        playlistInputDialog = CustomDialog(activity as Context)
        createPlaylistDialog = AddToPlaylist(activity as Context)
        recyclerViewAllSongs = view.findViewById(R.id.recyclerAllSongs)
        recyclerViewRecentTracks = view.findViewById(R.id.recyclerRecentTrack)

        createPlaylistDialog.setOnDismissListener {
            val playlistId: Int = createPlaylistDialog.selectedPlaylistId
            if (playlistId != -1) {
                Toast.makeText(
                    activity as Context,
                    "$selectedSongId added to $playlistId",
                    Toast.LENGTH_SHORT
                ).show()
                //TODO: ADD selectedSongId to playlistId
            } else {
                Toast.makeText(
                    activity as Context,
                    "No Playlist Selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }






        if (activity != null) {

            adapterAllSongs =
                AllSongsAapter(activity as Context)
            adapterRecentTracks =
                RecentTracksAdapter(activity as Context)

            recyclerViewAllSongs.adapter = adapterAllSongs
            recyclerViewRecentTracks.adapter = adapterRecentTracks

            recyclerViewAllSongs.layoutManager = LinearLayoutManager(activity)
            val horizontalLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewRecentTracks.layoutManager = horizontalLayoutManager

            recyclerViewAllSongs.addItemDecoration(
                DividerItemDecoration(
                    recyclerViewAllSongs.context,
                    (recyclerViewAllSongs.layoutManager as LinearLayoutManager).orientation
                )
            )


        }

        return view
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        try {
            selectedSongId = adapterAllSongs.getSelectedSongId()
        } catch (e: Exception) {
            e.printStackTrace()
            return super.onContextItemSelected(item)
        }
        when (item.itemId) {
            R.id.ctx_add_to_playlist -> {

                try {
                    createPlaylistDialog.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            else -> {

            }
        }

        return super.onContextItemSelected(item)
    }

}

class AddToPlaylist(
    context: Context
) : Dialog(context) {

    private var playlists: List<PlaylistEntity>? = null

    fun setDialogPlaylists(_playlists: List<PlaylistEntity>) {
        playlists = _playlists
    }


    var recyclerView: RecyclerView? = null
    var adapter: PlaylistDialogAdapter? = null

    var selectedPlaylistId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_to_playlist_dialog)


        recyclerView = findViewById(R.id.recyclerViewPlaylists)

        adapter = PlaylistDialogAdapter(context)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)

        adapter?.playlistClickCallback = fun(id: Int) {
            selectedPlaylistId = id
            dismiss()
        }

        adapter?.setPlayLists(playlists!!)

    }

}

