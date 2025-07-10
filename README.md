# 🎬 KrossPlay

**KrossPlay** is a simple yet powerful Kotlin Multiplatform (KMP) video player library designed to work seamlessly on Android and iOS using **Jetpack Compose** and **SwiftUI/UIKit** interoperability.

Supports:
- ✅ Android: ExoPlayer (Media3)
- ✅ iOS: AVPlayer
- ✅ Jetpack Compose integration
- ✅ Error callback handling
- ✅ Mute/Unmute
- ✅ Fullscreen toggle ready
- ✅ Minimal API for quick integration

---

## 📦 Dependency

### Gradle (Kotlin Multiplatform Project):
```kotlin
//commonMain
dependencies {
    implementation("io.github.farimarwat:krossplay:1.1")
}
```
## 🚀 Usage

### 1️⃣ Create and remember the player state

```kotlin
val krossPlayState = rememberKrossPlayState().apply {
    setOnErrorListener { errorMessage ->
        println("Playback Error: $errorMessage")
    }
}
```

---

### 2️⃣ Load a video URL

```kotlin
krossPlayState.loadVideo("https://your-stream-url.com/video.mp4")
```

---

### 3️⃣ Use the player UI

```kotlin
KrossMediaPlayer(
    modifier = Modifier
        .fillMaxWidth()
        .height(300.dp),
    playerState = krossPlayState,
    onToggleFullScreen = {
        // Handle fullscreen toggle logic here
    }
)
```
