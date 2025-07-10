package com.farimarwat.krossplaydemo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farimarwat.krossplay.media.KrossMediaPlayer
import com.farimarwat.krossplay.media.rememberKrossPlayState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import krossplaydemo.composeapp.generated.resources.Res
import krossplaydemo.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var fullScreen by remember { mutableStateOf(false)}
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var url by remember { mutableStateOf("https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8")}
            val krossPlayState = rememberKrossPlayState().apply {
                setOnErrorListener { errorMessage ->
                    println("Playback Error: $errorMessage")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = url,
                    onValueChange = {
                        url = it
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter text") },
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        krossPlayState.loadVideo(url)
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play"
                    )
                }
            }

            KrossMediaPlayer(
                modifier = if(fullScreen){
                    Modifier.fillMaxSize()
                }else{
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                },
                playerState = krossPlayState,
                onToggleFullScreen = {
                    fullScreen = !fullScreen
                }
            )
        }
    }
}