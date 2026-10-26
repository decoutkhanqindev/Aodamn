package com.decoutkhanqindev.aodamn.data.device.media

import android.app.Application
import android.graphics.Bitmap
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.core.graphics.scale
import com.decoutkhanqindev.aodamn.data.device.notification.NotificationStateManager
import com.decoutkhanqindev.aodamn.utils.Tag
import com.decoutkhanqindev.aodamn.utils.collectCatching
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MediaStateManager(
    private val app: Application,
    notificationStateManager: NotificationStateManager,
) : Tag {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val mainHandler = Handler(Looper.getMainLooper())
    private var controller: MediaController? = null

    private var artSource: Bitmap? = null
    private var art: Bitmap? = null

    private val controllerCallback = object : MediaController.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackState?) = publish()

        override fun onMetadataChanged(metadata: MediaMetadata?) = publish()

        override fun onSessionDestroyed() = attach(token = null)
    }

    private val _playback = MutableStateFlow<MediaPlayback?>(null)
    val playback: StateFlow<MediaPlayback?> = _playback.asStateFlow()

    init {
        scope.launch {
            notificationStateManager.mediaSessionToken.collectCatching(
                block = { token -> attach(token) },
                catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
            )
        }
    }

    fun playPause() {
        val transportControls = controller?.transportControls ?: return
        if (_playback.value?.isPlaying == true) transportControls.pause() else transportControls.play()
    }

    fun skipToPrevious() {
        controller?.transportControls?.skipToPrevious()
    }

    fun skipToNext() {
        controller?.transportControls?.skipToNext()
    }

    private fun attach(token: MediaSession.Token?) {
        if (token == controller?.sessionToken) return
        controller?.unregisterCallback(controllerCallback)
        controller = token?.let { MediaController(app, it) }
        controller?.registerCallback(controllerCallback, mainHandler)
        publish()
    }

    private fun publish() {
        _playback.value = controller?.toMediaPlayback()
    }

    private fun MediaController.toMediaPlayback(): MediaPlayback? {
        val metadata = metadata ?: return null
        val title = metadata.getString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE)
            ?: metadata.getString(MediaMetadata.METADATA_KEY_TITLE)
        if (title.isNullOrBlank()) return null
        val state = playbackState
        val actions = state?.actions ?: 0L
        return MediaPlayback(
            title = title,
            artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST)
                ?.takeIf { it.isNotBlank() },
            isPlaying = (state?.state ?: PlaybackState.STATE_NONE) in PLAYING_STATES,
            canSkipToPrevious = actions and PlaybackState.ACTION_SKIP_TO_PREVIOUS != 0L,
            canSkipToNext = actions and PlaybackState.ACTION_SKIP_TO_NEXT != 0L,
            progress = state?.toMediaProgress(durationMillis = metadata.getLong(MediaMetadata.METADATA_KEY_DURATION)),
            albumArt = metadata.toAlbumArt(),
        )
    }

    private fun MediaMetadata.toAlbumArt(): Bitmap? {
        val source = getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
            ?: getBitmap(MediaMetadata.METADATA_KEY_ART)
            ?: getBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON)
        if (source === artSource) return art
        artSource = source
        art = source?.takeIf { it.width > 0 && it.height > 0 }?.let { bitmap ->
            val scale = ART_SIZE_PX.toFloat() / maxOf(bitmap.width, bitmap.height)
            if (scale >= 1f) {
                bitmap
            } else {
                bitmap.scale(
                    width = (bitmap.width * scale).toInt().coerceAtLeast(1),
                    height = (bitmap.height * scale).toInt().coerceAtLeast(1),
                )
            }
        }
        return art
    }

    private fun PlaybackState.toMediaProgress(durationMillis: Long): MediaProgress? {
        if (durationMillis <= 0L || position < 0L) return null
        return MediaProgress(
            durationMillis = durationMillis,
            positionMillis = position,
            updatedAtMillis = lastPositionUpdateTime.takeIf { it > 0L } ?: SystemClock.elapsedRealtime(),
            speed = if (state == PlaybackState.STATE_PLAYING) playbackSpeed else 0f,
        )
    }

    companion object {
        const val ART_SIZE_PX = 160

        private val PLAYING_STATES = setOf(
            PlaybackState.STATE_PLAYING,
            PlaybackState.STATE_BUFFERING,
            PlaybackState.STATE_CONNECTING,
            PlaybackState.STATE_FAST_FORWARDING,
            PlaybackState.STATE_REWINDING,
            PlaybackState.STATE_SKIPPING_TO_NEXT,
            PlaybackState.STATE_SKIPPING_TO_PREVIOUS,
            PlaybackState.STATE_SKIPPING_TO_QUEUE_ITEM,
        )
    }
}
