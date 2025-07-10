package com.farimarwat.krossplay.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentItem
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSURL
import platform.Foundation.removeObserver
import platform.darwin.NSEC_PER_SEC
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

actual class KrossPlayerState {

    var player:AVPlayer? = null
    private var timeObserver:Any? = null
    actual var isPlaying: Boolean by mutableStateOf(false)
    actual var progress: Float by mutableStateOf(0F)
    actual var duration: Long by mutableStateOf(0L)
    actual var currentPosition: Long by mutableStateOf(0L)

    @OptIn(ExperimentalForeignApi::class)
    actual fun loadVideo(url: String) {
        release()
        val nsUrl = NSURL.URLWithString(url)
        nsUrl?.let{nu ->
            player = AVPlayer(uRL = nu).apply {
                timeObserver = addPeriodicTimeObserverForInterval(
                    interval = CMTimeMakeWithSeconds(1.0, preferredTimescale = NSEC_PER_SEC.toInt())
                    ,
                    queue = dispatch_get_main_queue()
                ) { time ->
                    isPlaying = player?.rate != 0f
                    if(duration == 0L){
                        player?.currentItem?.let {
                            val totalDuration = CMTimeGetSeconds(it.duration)
                            if (totalDuration.isFinite()) {
                                duration = (totalDuration * 1000).toLong() // Also in milliseconds
                            }
                        }
                    }
                    currentPosition = (CMTimeGetSeconds(time) * 1000).toLong() // In milliseconds
                    progress = if (duration > 0) currentPosition.toFloat() / duration else 0f
                    println("Progress: ${progress}")
                }
            }

            player?.play()
        }
    }

    actual fun togglePlay() {
        if(isPlaying){
            player?.pause()
        } else {
            player?.play()
        }
        isPlaying = !isPlaying
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun seekTo(progress: Float) {
        val targetTimeInSeconds = progress * duration / 1000.0 // duration is in milliseconds, so we divide by 1000
        val targetTime = CMTimeMakeWithSeconds(targetTimeInSeconds, preferredTimescale = NSEC_PER_SEC.toInt())
        player?.seekToTime(targetTime)
    }


    actual fun release() {
        if (timeObserver != null) {
            (timeObserver as? NSObject)?.let { observer ->
                try {
                    player?.removeTimeObserver(observer)
                    player?.currentItem?.removeObserver(observer, "duration")
                } catch (_: Exception) {
                    // Safely ignore if already removed or invalid
                }
            }
            timeObserver = null
        }

        player?.replaceCurrentItemWithPlayerItem(null)
        player = null
        isPlaying = false
        progress = 0f
        duration = 0L
        currentPosition = 0L
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