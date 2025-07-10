package com.farimarwat.krossplay.media

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
import androidx.compose.ui.viewinterop.UIKitView
import com.farimarwat.krossplay.ui.MediaControls
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.AVFoundation.AVLayerVideoGravityResizeAspect
import platform.AVFoundation.AVPlayerLayer
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun KrossMediaPlayer(
    modifier: Modifier,
    playerState: KrossPlayerState,
    onToggleFullScreen: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        val playbackLayer = remember{AVPlayerLayer()}.apply {
            player = playerState.player
            videoGravity = AVLayerVideoGravityResizeAspect
        }

        UIKitView(
            factory = {
                val container = object : UIView(CGRectZero.readValue()) {
                    override fun layoutSubviews() {
                        super.layoutSubviews()
                        playbackLayer.frame = bounds
                    }
                }
                container.layer.addSublayer(playbackLayer)
                container
            },
            update = { container ->
                // Force the layer to update its display
                playbackLayer.setNeedsDisplay()
                container.setNeedsLayout()
                container.layoutIfNeeded()
            },
            modifier = Modifier.fillMaxSize()
        )
        MediaControls(
            state = playerState,
            onFullScreenClicked = {
                onToggleFullScreen()
            }
        )
    }
}