package com.dhl.musicplayer.adapters

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dhl.musicplayer.R
import com.dhl.musicplayer.interfaces.RecycleViewItemClickListener
import com.dhl.musicplayer.models.SongModel

/**
 * Created by Hitesh on 2/21/2018.
 */
class SongListAdapter(songs: List<SongModel>, val itemClickListener: RecycleViewItemClickListener)
    : RecyclerView.Adapter<SongListAdapter.SongListViewHolder>() {

    val mSongList = songs

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SongListViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.row_songs, parent, false)
        return SongListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongListViewHolder?, position: Int) {
        val mSong = mSongList[position]
        holder!!.tvSongTitle.text = mSong.mSongName
        holder.tvDuration.text = mSong.mSongDuration
    }

    override fun getItemCount(): Int {
        return mSongList.size
    }

    inner class SongListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvSongTitle: AppCompatTextView;
        var tvDuration: AppCompatTextView;
        var ivSongAvtar: AppCompatImageView;

        init {
            tvSongTitle = itemView.findViewById(R.id.tvSongTitle) as AppCompatTextView
            tvDuration = itemView.findViewById(R.id.tvDuration) as AppCompatTextView
            ivSongAvtar = itemView.findViewById(R.id.ivSongAvtar) as AppCompatImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemClickListener?.onItemClick(v!!, adapterPosition)
        }
    }
}