package com.farimarwat.krossplay.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSURL
import platform.Foundation.removeObserver
import platform.darwin.NSEC_PER_SEC
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

actual class KrossPlayerState {

    var player: AVPlayer = AVPlayer() // Always reuse the same player
    private var timeObserver: Any? = null

    actual var isPlaying: Boolean by mutableStateOf(false)
    actual var progress: Float by mutableStateOf(0F)
    actual var duration: Long by mutableStateOf(0L)
    actual var currentPosition: Long by mutableStateOf(0L)
    actual var volumeMuted: Boolean by mutableStateOf(false)

    private var isObserving = false

    actual var errorCallback: ((String) -> Unit)? = null


    @OptIn(ExperimentalForeignApi::class)
    actual fun loadVideo(url: String) {
        release()
        val nsUrl = NSURL.URLWithString(url)
        nsUrl?.let { nu ->
            val item = AVPlayerItem.playerItemWithURL(URL = nu)

            NSNotificationCenter.defaultCenter.addObserverForName(
                name = AVPlayerItemFailedToPlayToEndTimeNotification,
                `object` = item,
                queue = null
            ) { _ ->
                val error = item.error?.localizedDescription ?: "Unknown playback error"
                errorCallback?.invoke(error)
            }

            player.replaceCurrentItemWithPlayerItem(item)

            // ⏱️ Time observer for progress updates
            timeObserver = player.addPeriodicTimeObserverForInterval(
                interval = CMTimeMakeWithSeconds(1.0, preferredTimescale = NSEC_PER_SEC.toInt()),
                queue = dispatch_get_main_queue()
            ) { time ->
                isObserving = true
                isPlaying = player.rate != 0f
                if (duration == 0L) {
                    player.currentItem?.let {
                        val totalDuration = CMTimeGetSeconds(it.duration)
                        if (totalDuration.isFinite()) {
                            duration = (totalDuration * 1000).toLong()
                        }
                    }
                }
                currentPosition = (CMTimeGetSeconds(time) * 1000).toLong()
                progress = if (duration > 0) currentPosition.toFloat() / duration else 0f
            }

            player.play()
        }
    }

    actual fun togglePlay() {
        if (isPlaying) {
            player.pause()
        } else {
            player.play()
        }
        isPlaying = !isPlaying
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun seekTo(progress: Float) {
        val targetTimeInSeconds = progress * duration / 1000.0
        val targetTime = CMTimeMakeWithSeconds(targetTimeInSeconds, preferredTimescale = NSEC_PER_SEC.toInt())
        player.seekToTime(targetTime)
    }

    actual fun setMuted(muted:Boolean){
        this@KrossPlayerState.volumeMuted = muted
        player.setMuted(muted)
    }
    actual fun release() {
        if (timeObserver != null && isObserving) {
            (timeObserver as? NSObject)?.let { observer ->
                try {
                    player.removeTimeObserver(observer)
                    player.currentItem?.removeObserver(observer, "duration")
                } catch (e: Exception) {
                    errorCallback?.invoke("$e")
                }
                isObserving = false
            }
            timeObserver = null
        }

        player.replaceCurrentItemWithPlayerItem(null)
        isPlaying = false
        progress = 0f
        duration = 0L
        currentPosition = 0L
    }
    actual fun setOnErrorListener(callback: (String) -> Unit) {
        errorCallback = callback
    }
}

@Composable
actual fun rememberKrossPlayState(): KrossPlayerState {
    return remember { KrossPlayerState() }.also {
        DisposableEffect(it) {
            onDispose { it.release() }
        }
    }
}
