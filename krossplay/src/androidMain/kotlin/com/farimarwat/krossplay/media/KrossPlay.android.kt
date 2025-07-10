package com.farimarwat.krossplay.media

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import com.farimarwat.krossplay.ui.MediaControls

@Composable
actual fun KrossMediaPlayer(
    modifier: Modifier,
    playerState: KrossPlayerState,
    onToggleFullScreen: (Boolean) -> Unit
) {
    var fullScreen by remember { mutableStateOf(false) }
    Box(
        modifier = if(fullScreen){
            Modifier.fillMaxSize()
        } else {
            Modifier
                .fillMaxWidth()
                .height(300.dp)
        }
            .then(modifier
                .background(Color.Black))
    ){
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = playerState.player
                    useController = false
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { playerview ->
                playerview.player = playerState.player
            }
        )
        MediaControls(
            state = playerState,
            onFullScreenClicked = {
                fullScreen = !fullScreen
                onToggleFullScreen(fullScreen)
            }
        )
    }
}