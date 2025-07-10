package com.farimarwat.krossplay.media

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
expect fun KrossMediaPlayer(
    modifier:Modifier,
    playerState: KrossPlayerState,
    onToggleFullScreen:()->Unit = {}
)