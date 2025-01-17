package com.amantech.musicplayer.adapters

import android.content.Context
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amantech.musicplayer.R
import com.amantech.musicplayer.database.playlists.PlaylistEntity

class PlaylistAdapter(context: Context) :
    RecyclerView.Adapter<PlaylistAdapter.AllPlaylistViewHolder>() {

    private var selectedPlaylist: PlaylistEntity? = null


    private fun setSelectedPlaylist(p: PlaylistEntity) {
        selectedPlaylist = p
    }

    fun getSelectedPlaylist(): PlaylistEntity? = selectedPlaylist

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    private var playlists: List<PlaylistEntity>? = null
    var onPlaylistClickCallback: ((playlist: PlaylistEntity) -> Unit)? = null

    class AllPlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {
        val playlistName: TextView = view.findViewById(R.id.playlistName)
        val playlistCardView: LinearLayout = view.findViewById(R.id.PlaylistsCardView)

        init {
            view.setOnCreateContextMenuListener(this)
        }


        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu!!.add(0, R.id.ctx_remove_playlist, 0, "Remove  Playlist")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPlaylistViewHolder {
        val playlistItemView: View = mInflater.inflate(R.layout.single_playlist_item, parent, false)
        return AllPlaylistViewHolder(
            playlistItemView
        )
    }

    override fun onBindViewHolder(holder: AllPlaylistViewHolder, position: Int) {
        val currentPlaylist: PlaylistEntity = playlists!![position]
        holder.playlistName.text = currentPlaylist.name

        holder.playlistCardView.setOnLongClickListener {
            setSelectedPlaylist(currentPlaylist)
            Log.i("SELECTEDPLAYLIST",selectedPlaylist.toString())
            false
        }

        holder.playlistCardView.setOnClickListener {
            onPlaylistClickCallback?.invoke(currentPlaylist)
        }


    }

    fun setPlayLists(mplaylists: List<PlaylistEntity>) {
        playlists = mplaylists
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (playlists != null)
            playlists!!.size;
        else 0;
    }

}
