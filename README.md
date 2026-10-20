# Aquarium - Always-On Kiosk App

**Aquarium** is an always-on Android kiosk application designed to display a web interface (e.g., smart home dashboards, status displays, web dashboards) in full screen without screen timeouts or visible system bars.

---

## Features

- 📺 **Native WebView Component**: Displays any web page (`http://` or `https://`) with full JavaScript, DOM storage, and cleartext HTTP support.
- ⚡ **Always-On Display**: Uses `FLAG_KEEP_SCREEN_ON` to prevent the device screen from turning off or going to sleep.
- 🖼️ **Fullscreen Immersive Kiosk Mode**: Automatically hides the top status bar and bottom navigation bar for a clean, full-screen view.
- 🌙 **Scheduled Screen Dimming**: Configurable start and end times for screen dimming (e.g., night mode from 22:00 to 07:00), automatically handling time schedules across midnight.
- 💡 **Dual Brightness Levels**: Separate customizable brightness settings for normal active hours and dimmed hours.
- ⚙️ **On-Screen Settings**: Easily configure settings at any time via a subtle floating gear button.
- 💾 **Persistent Settings**: Automatically saves configuration locally using `SharedPreferences`.
- 🤖 **Proudly vibe coded**: 100% AI slop

---

## Tech Stack & Architecture

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose + Material 3
- **Architecture**: MVVM with `ViewModel` and `StateFlow`
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 37

### Project Structure

```text
app/src/main/java/org/mcfish/aquarium/
├── MainActivity.kt               # Entry point, immersive mode & screen brightness window control
├── data/
│   ├── KioskSettings.kt          # Settings data model
│   └── KioskSettingsRepository.kt# Persistent preferences storage
└── ui/
    ├── KioskViewModel.kt         # Time checking & state calculation logic
    ├── KioskScreen.kt            # Native WebView composable
    ├── SettingsDialog.kt         # Material 3 settings dialog UI
    └── theme/                    # Material 3 theme definitions
```

---

## Usage & Configuration

### Opening Settings
When running in kiosk mode, tap the semi-transparent **⚙ Gear button** in the top-right corner to open the configuration menu.

### Settings Options
- **URL to load**: The web address to render (e.g., `http://192.168.1.100:8123` or `https://example.com`).
- **Screen Dimming Schedule**: Enable/disable automatic dimming and set 24-hour start/end times (`HH:mm`).
- **Brightness Levels**: Set sliders for normal and dimmed mode brightness (1% to 100% each).

---

## Building & Running

### Prerequisites
- Android Studio Ladybug (or newer)
- Android SDK 37 / JDK 11+

### Gradle Build
Build the debug APK using the Gradle wrapper:

```bash
./gradlew assembleDebug
```

Run unit tests:

```bash
./gradlew testDebugUnitTest
```

---

## License

This project is licensed under the MIT License.
