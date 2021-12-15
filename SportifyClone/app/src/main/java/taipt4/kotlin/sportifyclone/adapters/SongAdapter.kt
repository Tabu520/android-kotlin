package taipt4.kotlin.sportifyclone.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.RequestManager
import taipt4.kotlin.sportifyclone.R
import taipt4.kotlin.sportifyclone.data.entities.Song
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private val glide: RequestManager
) : BaseSongAdapter(R.layout.list_item) {

    override val differ: AsyncListDiffer<Song> = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        val tvPrimary: TextView = holder.itemView.findViewById(R.id.tvPrimary)
        val tvSecondary: TextView = holder.itemView.findViewById(R.id.tvSecondary)
        val ivItemImage: ImageView = holder.itemView.findViewById(R.id.ivItemImage)
        tvPrimary.text = song.title
        tvSecondary.text = song.subtitle
        glide.load(song.imageUrl).into(ivItemImage)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(song)
            }
        }

    }

}