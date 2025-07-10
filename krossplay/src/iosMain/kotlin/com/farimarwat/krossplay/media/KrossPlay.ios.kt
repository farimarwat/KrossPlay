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
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerLayer
import platform.AVKit.AVPlayerViewController
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.UIKit.NSLayoutConstraint
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

        val avPlayerViewController = remember { AVPlayerViewController() }

        avPlayerViewController.player = playerState.player
        avPlayerViewController.showsPlaybackControls = true
        avPlayerViewController.allowsPictureInPicturePlayback = true

        UIKitView(
            factory = {
                val playerContainer = UIView()

                avPlayerViewController.view.translatesAutoresizingMaskIntoConstraints = false
                playerContainer.addSubview(avPlayerViewController.view)

                NSLayoutConstraint.activateConstraints(
                    listOf(
                        avPlayerViewController.view.leadingAnchor.constraintEqualToAnchor(playerContainer.leadingAnchor),
                        avPlayerViewController.view.trailingAnchor.constraintEqualToAnchor(playerContainer.trailingAnchor),
                        avPlayerViewController.view.topAnchor.constraintEqualToAnchor(playerContainer.topAnchor),
                        avPlayerViewController.view.bottomAnchor.constraintEqualToAnchor(playerContainer.bottomAnchor)
                    )
                )

                playerContainer
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