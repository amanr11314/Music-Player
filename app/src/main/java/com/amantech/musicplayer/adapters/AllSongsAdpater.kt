package com.amantech.musicplayer.adapters


import android.content.Context
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.amantech.musicplayer.R
import com.amantech.musicplayer.database.allSongs.SongEntity

class AllSongsAdapter(context: Context) : RecyclerView.Adapter<AllSongsAdapter.AllSongsViewHolder>() {

    private var selectedSongId: Int = -1


    private fun setSelectedSongId(p: Int) {
        selectedSongId = p
    }

    fun getSelectedSongId(): Int = selectedSongId


    val mInflater: LayoutInflater = LayoutInflater.from(context)

    private var songs: List<SongEntity>? = null

    //callbacks for item click listeners fro updating live data
    var favClickCallback: ((id: Int) -> Unit)? = null
    var onSongClickCallback: ((song: SongEntity, allSongs:List<SongEntity>) -> Unit)? = null


    class AllSongsViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {
        val txtSongName: TextView = view.findViewById(R.id.txtSongName)
        val txtSongArtistName: TextView = view.findViewById(R.id.txtSongArtistName)
        val btnFav: ToggleButton = view.findViewById(R.id.btnFav)
        val cardViewForSong: CardView = view.findViewById(R.id.cardViewForSong)

        init {
            view.setOnCreateContextMenuListener(this)
        }


        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu!!.add(0, R.id.ctx_add_to_playlist, 0, "Add To Playlist")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllSongsViewHolder {
        val songItemView: View = mInflater.inflate(R.layout.single_song_item, parent, false)
        return AllSongsViewHolder(
            songItemView
        )
    }

    override fun onViewRecycled(holder: AllSongsViewHolder) {
        holder.cardViewForSong.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: AllSongsViewHolder, position: Int) {
        if (songs != null) {
            val currentSong: SongEntity = songs!![position]
            holder.txtSongName.text = currentSong.songName
            holder.txtSongArtistName.text = currentSong.artistName
            holder.btnFav.isChecked = songs!![position].isFav > 0

            holder.cardViewForSong.setOnLongClickListener {
                setSelectedSongId(currentSong.songId)
                false
            }

            holder.btnFav.setOnClickListener {
                favClickCallback?.invoke(currentSong.songId)
                Log.d("ALLSONGINFO", songs.toString())
            }

            holder.cardViewForSong.setOnClickListener {
                onSongClickCallback?.invoke(
                    currentSong, songs!!
                )
            }
        } else {
            holder.txtSongName.setText(R.string.NoSong)
        }
    }

    fun setSongs(mSongs: List<SongEntity>) {
        songs = mSongs
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (songs != null)
            songs!!.size;
        else 0;
    }
}