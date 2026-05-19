# Inspi — Android App

> "Build your creative habit, one day at a time."

## About
Inspi is a creative habit-building app for Photography and Drawing.  
Like Duolingo, but for hobbies — daily tasks, streaks, XP, an AI coach, and a personal gallery.

---

## Project Structure

```
com.inspi.app/
├── ui/
│   ├── onboarding/          Splash / routing screen
│   ├── hobbyselection/      Pick Photography or Drawing
│   ├── home/                Dashboard: streak, daily task, weekly challenge
│   ├── taskcomplete/        Camera or gallery picker + submission
│   ├── gallery/             Grid of all past submissions + Flashback
│   ├── coach/               AI chat with Claude (gemini-2.0-flash)
│   ├── profile/             XP, stats, settings
│   ├── navigation/          NavGraph + BottomBar
│   └── theme/               Colors, Typography, Theme
├── data/
│   ├── local/               Room DB, DAOs, Entities
│   ├── preferences/         DataStore (hobby, notifications)
│   └── repository/          All data access — no direct DAO calls from ViewModels
├── domain/
│   ├── models/              UserProfile, Submission, DailyTask, etc.
│   └── usecases/            (reserved for future use-cases)
├── network/
│   └── ClaudeApiService.kt  Real + Fake implementations
├── di/
│   └── AppModule.kt         Hilt modules
└── utils/
    ├── XpCalculator.kt
    ├── StreakManager.kt
    ├── TaskSelector.kt
    ├── DailyReminderWorker.kt
    └── Analytics.kt         Stub interface
```

---

## Quick Start

### 1. Clone & open in Android Studio
```
git clone <repo-url>
```
Open the root `inspi/` folder in Android Studio Hedgehog (2023.1.1) or newer.

### 2. Add your Gemini API key
Create `local.properties` in the project root (copy from `local.properties.template`):
```
GEMINI_API_KEY=sk-ant-xxxxxxxxxxxxxxxxxxxx
```
⚠️ `local.properties` is in `.gitignore` and must **never** be committed.

### 3. Add mascot assets
Place these files in `app/src/main/res/drawable/`:
- `mascot_inspi.png`  — neutral / waving pose
- `mascot_inspi_celebrate.png` — celebrating pose (hands up)

The mascot has a black background — use `background = Color.Transparent` or set  
`android:background="@color/transparent"` to blend with `#F6FAF1`.

### 4. Build & run
```
./gradlew assembleDebug
```
Min SDK: **26 (Android 8.0)**

---

## Design System — "Pure Growth"

| Color | HEX | Role |
|-------|-----|------|
| Primary | `#8272D6` | CTA buttons, active tabs |
| Accent | `#C0DD97` | Progress bar, completed tasks |
| Background | `#F6FAF1` | App background |
| Text | `#1A2410` | Headings, body |
| Highlight | `#EDE9FF` | Selected state, chips |

---

## API Key Security
- **Prototype**: stored in `local.properties` → injected via `BuildConfig.GEMINI_API_KEY`
- **Production**: route requests through a lightweight backend proxy — never ship the key in an APK

---

## Running Tests
```
./gradlew test
```
Unit tests cover:
- `XpCalculatorTest` — XP math, multipliers, level calculation
- `StreakManagerTest` — streak continuity, reset, missed-day logic
- `TaskSelectorTest` — day-modulo task selection

---

## Architecture
- **MVVM** with `StateFlow<UiState<T>>` per screen
- **Hilt** for dependency injection
- **Room** as single source of truth for all persistent data
- **DataStore** for hobby selection and notification prefs
- **Offline-first** — only AI Coach requires internet
- **Jetpack Compose + Material 3**
- **CameraX** for in-app photography capture

---

## Notes for Future Versions
- Personal mascot customization (profile screen has placeholder avatar)
- Analytics interface stubbed in `Analytics.kt` — wire up Firebase/Mixpanel there
- Social / feed features intentionally excluded
- Additional hobbies: add a new pool to `TaskSelector.kt` and a new `HobbyType` enum entry
