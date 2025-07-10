package com.farimarwat.krossplay.media


import androidx.compose.runtime.*

expect class KrossPlayerState {
    var isPlaying: Boolean
    var progress: Float
    var duration: Long
    var currentPosition: Long

    fun loadVideo(url: String)
    fun togglePlay()
    fun seekTo(progress: Float)
    fun release()
}

@Composable
expect fun rememberKrossPlayState(): KrossPlayerState
