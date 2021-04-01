package com.acmvit.c2c2021.ui.tracks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.databinding.ItemTrackBinding
import com.acmvit.c2c2021.models.Track

class TracksAdapter(
    private var tracks: List<Track>,
    private var onItemClickListener:(pos: Int) -> Unit = {}
): RecyclerView.Adapter<TracksAdapter.TracksVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTrackBinding.inflate(layoutInflater, parent, false)
        return TracksVH(binding)
    }

    fun setTracks(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TracksVH, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size

    inner class TracksVH(private val binding: ItemTrackBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { onItemClickListener(adapterPosition) }
        }

        fun bind(track: Track) {
            binding.track = track
            binding.executePendingBindings()
        }
    }
}