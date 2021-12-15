package taipt4.kotlin.sportifyclone.exoplayer.callbacks

import android.widget.Toast
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import taipt4.kotlin.sportifyclone.exoplayer.MusicService

class MusicPlayerEventListener(
    private val musicService: MusicService
) : Player.Listener {

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if (playbackState == Player.STATE_READY) {
            musicService.stopForeground(false)
        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicService, "An unknown error occurred!", Toast.LENGTH_LONG).show()
    }
}