# Installation Guide

## Prerequisites

- [Android Studio](https://developer.android.com/studio) Ladybug (2024.2.1) or newer
- JDK 17 (bundled with recent Android Studio versions)
- Android SDK Platform 35
- An Android device or emulator running **API 26 (Android 8.0)** or higher

## 1. Clone the repository

```bash
git clone https://github.com/bankaimodder/expense-tracker-android.git
cd expense-tracker-android
```

## 2. Open the project

Open the cloned folder in Android Studio (`File > Open`) and let Gradle sync
finish. Android Studio will download the correct Gradle distribution
automatically via the included Gradle wrapper (`./gradlew`).

## 3. Build from the command line (optional)

```bash
./gradlew assembleDebug
```

The debug APK is generated at:

```
app/build/outputs/apk/debug/app-debug.apk
```

## 4. Run the app

- **From Android Studio:** select the `app` run configuration and click
  **Run** with a connected device or emulator selected.
- **From the command line:**

```bash
./gradlew installDebug
```

## 5. Run the tests

Unit tests (domain layer use cases, run on the JVM):

```bash
./gradlew testDebugUnitTest
```

## Project configuration

No API keys, backend services, or `google-services.json` are required — the
app is fully offline and stores all data locally with Room and DataStore.

## Troubleshooting

- **Gradle sync fails on first open:** make sure you have an internet
  connection the first time, since Gradle needs to download dependencies
  from Google's and Maven Central's repositories.
- **"SDK location not found":** create a `local.properties` file in the
  project root with `sdk.dir=/path/to/your/Android/sdk`, or let Android
  Studio generate it automatically on first sync.
- **Minimum SDK errors on an old emulator:** the app targets a minimum of
  API 26; create or use an emulator running API 26 or later.
