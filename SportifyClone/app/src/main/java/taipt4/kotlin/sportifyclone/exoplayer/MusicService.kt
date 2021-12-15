package taipt4.kotlin.sportifyclone.exoplayer

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import taipt4.kotlin.sportifyclone.exoplayer.callbacks.MusicPlaybackPreparer
import taipt4.kotlin.sportifyclone.exoplayer.callbacks.MusicPlayerEventListener
import taipt4.kotlin.sportifyclone.exoplayer.callbacks.MusicPlayerNotificationListener
import taipt4.kotlin.sportifyclone.other.Constants.MEDIA_ROOT_ID
import taipt4.kotlin.sportifyclone.other.Constants.NETWORK_ERROR
import javax.inject.Inject

private const val SERVICE_TAG = "MusicService"

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var firebaseMusicSource: FirebaseMusicSource

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private lateinit var musicNotificationManager: MusicNotificationManager

    var isForegroundService = false
    private var currentPlayingSong: MediaMetadataCompat? = null
    private var isPlayerInitialized: Boolean = false

    private lateinit var musicEventListener: MusicPlayerEventListener

    companion object {
        var currentSongDuration = 0L
            private set
    }

    override fun onCreate() {
        super.onCreate()
        serviceScope.launch {
            firebaseMusicSource.fetchMediaData()
        }
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken

        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            MusicPlayerNotificationListener(this),
        ) {
            if(exoPlayer.duration != C.TIME_UNSET) {
                currentSongDuration = exoPlayer.duration
            }
        }

        val musicPlaybackPreparer =
            MusicPlaybackPreparer(firebaseMusicSource = firebaseMusicSource) {
                currentPlayingSong = it
                preparePlayer(
                    firebaseMusicSource.songs,
                    it,
                    true
                )
            }

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
        mediaSessionConnector.setPlayer(exoPlayer)

        musicEventListener = MusicPlayerEventListener(this)
        exoPlayer.addListener(musicEventListener)
        musicNotificationManager.showNotification(exoPlayer)
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val currentSongIndex = if (currentPlayingSong == null) 0 else songs.indexOf(itemToPlay)
        exoPlayer.setMediaSource(firebaseMusicSource.asMediaSource(dataSourceFactory))
        exoPlayer.prepare()
        exoPlayer.seekTo(currentSongIndex, 0L)
        exoPlayer.playWhenReady = playNow
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultsSent = firebaseMusicSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(firebaseMusicSource.asMediaItems())
                        if (!isPlayerInitialized && firebaseMusicSource.songs.isNotEmpty()) {
                            preparePlayer(
                                firebaseMusicSource.songs,
                                firebaseMusicSource.songs[0],
                                false
                            )
                            isPlayerInitialized = true
                        }
                    } else {
                        mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    result.detach()
                }
            }
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        serviceScope.cancel()

        exoPlayer.release()
        exoPlayer.removeListener(musicEventListener)
        super.onDestroy()
    }

    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return firebaseMusicSource.songs[windowIndex].description
        }
    }
}