package com.amantech.musicplayer.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.amantech.musicplayer.R
import com.amantech.musicplayer.database.allSongs.SongEntity

class SongQueueAdapter (context: Context,dataSet: List<SongEntity> = emptyList()
) : DragDropSwipeAdapter<SongEntity, SongQueueAdapter.SongQueueViewHolder>(dataSet) {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var songs: List<SongEntity>? = null

    //callbacks for item click listeners fro updating live data
    var favClickCallback: ((id: Int) -> Unit)? = null
    var onSongClickCallback: (( song: SongEntity, allFavSongs: List<SongEntity>) -> Unit)? = null
    var currentPlayingSetSelected: ((currentSong: SongEntity, cardViewOfSong:RelativeLayout, cardView:CardView) -> Unit)? = null


    class SongQueueViewHolder(view: View) : DragDropSwipeAdapter.ViewHolder(view) {
        val txtSongName: TextView = view.findViewById(R.id.txtSongName)
        val txtSongArtistName: TextView = view.findViewById(R.id.txtSongArtistName)
        val btnFav: ToggleButton = view.findViewById(R.id.btnFav)
        var cardViewForSong: CardView = view.findViewById(R.id.cardViewForSong)
        var relativeLayoutCard:RelativeLayout = view.findViewById(R.id.relativeLayoutCard)
        val dragIcon: ImageView = view.findViewById(R.id.drag_icon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongQueueViewHolder {
        val songItemView: View = mInflater.inflate(R.layout.single_song_draggable, parent, false)
        return SongQueueViewHolder(
            songItemView
        )
    }


    fun setSongs(mSongs: List<SongEntity>) {
        songs = mSongs
        dataSet = mSongs
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (songs != null)
            songs!!.size
        else 0
    }

    override fun getViewHolder(itemView: View): SongQueueViewHolder {
        return SongQueueViewHolder(itemView)
    }

    override fun getViewToTouchToStartDraggingItem(
        item: SongEntity,
        viewHolder: SongQueueViewHolder,
        position: Int
    ): View? {
        return viewHolder.dragIcon
    }


    override fun onBindViewHolder(
        item: SongEntity,
        viewHolder: SongQueueViewHolder,
        position: Int
    ) {
        if (songs != null) {

            val currentSong: SongEntity = songs!![position]
            viewHolder.txtSongName.text = currentSong.songName
            viewHolder.txtSongArtistName.text = currentSong.artistName
            viewHolder.btnFav.isChecked = songs!![position].isFav > 0

            currentPlayingSetSelected?.invoke(currentSong,viewHolder.relativeLayoutCard,viewHolder.cardViewForSong )

            viewHolder.btnFav.setOnClickListener {
                favClickCallback?.invoke(currentSong.songId)
                Log.d("SINGLE PLAYLIST INFO", songs.toString())
            }

            viewHolder.cardViewForSong.setOnClickListener {
                onSongClickCallback?.invoke(
                    currentSong,
                    songs!!
                )
            }
        } else {
            viewHolder.txtSongName.setText(R.string.NoSong)
        }
    }
}