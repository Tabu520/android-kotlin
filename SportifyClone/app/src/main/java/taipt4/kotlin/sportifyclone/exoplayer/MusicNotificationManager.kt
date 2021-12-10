package taipt4.kotlin.sportifyclone.exoplayer

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import taipt4.kotlin.sportifyclone.other.Constants.NOTIFICATION_CHANNEL_ID
import taipt4.kotlin.sportifyclone.other.Constants.NOTIFICATION_ID

class MusicNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val newSongCallback: () -> Unit
) {

    private val notificationManager: PlayerNotificationManager.Builder

    init {
        notificationManager = PlayerNotificationManager.Builder(
            context,
            NOTIFICATION_ID,
            NOTIFICATION_CHANNEL_ID
        ).setNotificationListener(notificationListener)
    }
}