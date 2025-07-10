package com.farimarwat.krossplay.media


import androidx.compose.runtime.*

expect class KrossPlayerState {
    var isPlaying: Boolean
    var progress: Float
    var duration: Long
    var currentPosition: Long

    var volumeMuted: Boolean
    var errorCallback: ((String) -> Unit)?

    fun loadVideo(url: String)
    fun togglePlay()
    fun seekTo(progress: Float)
    fun setMuted(muted:Boolean)
    fun release()

    fun setOnErrorListener(callback: (String) -> Unit)
}

@Composable
expect fun rememberKrossPlayState(): KrossPlayerState
