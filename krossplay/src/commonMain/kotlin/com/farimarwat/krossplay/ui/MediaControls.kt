package com.farimarwat.krossplay.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.farimarwat.krossplay.media.KrossPlayerState
import com.farimarwat.krossplay.utils.formatAsTime

@Composable
fun MediaControls(
    state: KrossPlayerState,
    onFullScreenClicked:()->Unit={}
) {
    var showControls by remember { mutableStateOf(true) }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                showControls = !showControls
            }
    ) {
        var sliderPosition by remember { mutableStateOf(state.progress) }

        // Update slider position when not dragging and player progresses
        LaunchedEffect(state.progress) {
            sliderPosition = state.progress
        }

        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Play Button
                IconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.Black.copy(0.5f)),
                    onClick = {
                        state.togglePlay()
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                        imageVector = if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "Toggle Play",
                        tint = Color.White
                    )
                }

                // BottomBar
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(0.5f))
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Time
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(0.5f))
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Current Position
                        val textColor = Color.Black
                        Text(
                            text = state.currentPosition.formatAsTime(),
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor
                        )
                        Text(
                            text = "/",
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor
                        )
                        // Total Duration
                        Text(
                            text = state.duration.formatAsTime(),
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor
                        )
                    }

                    // Slider
                    Slider(
                        value = sliderPosition,
                        onValueChange = { newValue ->
                            sliderPosition = newValue
                        },
                        onValueChangeFinished = {
                            state.seekTo(sliderPosition)
                        },
                        valueRange = 0f..1f,
                        modifier = Modifier
                            .weight(1f)
                    )

                    // Fullscreen Button
                    IconButton(
                        modifier = Modifier
                            .size(24.dp),
                        onClick = {
                            onFullScreenClicked()
                        }
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Filled.Fullscreen,
                            contentDescription = "FullScreen",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}