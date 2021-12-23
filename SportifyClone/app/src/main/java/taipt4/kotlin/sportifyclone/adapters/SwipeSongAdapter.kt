package taipt4.kotlin.sportifyclone.adapters

import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import taipt4.kotlin.sportifyclone.R
import taipt4.kotlin.sportifyclone.data.entities.Song

class SwipeSongAdapter : BaseSongAdapter(R.layout.swipe_item) {

    override val differ: AsyncListDiffer<Song> = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        val tvPrimary2: TextView = holder.itemView.findViewById(R.id.tvPrimary2)

        val text = "${song.title} - ${song.subtitle}"
        tvPrimary2.text = text

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(song)
            }
        }
    }
}