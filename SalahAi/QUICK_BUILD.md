# SalahAi - Quick Build Guide

## TL;DR - Build in 5 Minutes

### Prerequisites
- Android Studio installed
- JDK 17+ installed
- Android SDK 34 installed

### Quick Steps

1. **Open Project**
   ```bash
   # Navigate to project folder
   cd SalahAi
   
   # Open in Android Studio
   open -a "Android Studio" .  # macOS
   # or just open Android Studio and select the folder
   ```

2. **Update Backend URL**
   - Open: `app/src/main/kotlin/com/salahtech/salahAi/data/api/ApiConfig.kt`
   - Change: `const val BASE_URL = "https://your-backend-url.com/"`
   - Save

3. **Build Debug APK**
   ```bash
   ./gradlew assembleDebug
   ```
   
   Or in Android Studio:
   - Build → Build Bundle(s) / APK(s) → Build APK(s)

4. **Find Your APK**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

5. **Install on Device/Emulator**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```
   
   Or in Android Studio:
   - Run → Run 'app'

## Build Release APK

```bash
# Create keystore (one-time)
keytool -genkey -v -keystore ~/salahAi.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias salahAi

# Build release APK
./gradlew assembleRelease
```

APK will be at: `app/build/outputs/apk/release/app-release.apk`

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Gradle sync fails | Run `./gradlew clean` then try again |
| Can't find APK | Check `app/build/outputs/apk/` directory |
| App won't install | Run `adb uninstall com.salahtech.salahAi` first |
| API connection fails | Verify backend URL in `ApiConfig.kt` |

## File Locations

- **Source Code**: `app/src/main/kotlin/com/salahtech/salahAi/`
- **Resources**: `app/src/main/res/`
- **Manifest**: `app/src/main/AndroidManifest.xml`
- **Build Config**: `app/build.gradle.kts`
- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`

## Next Steps

1. ✅ Build and test the app
2. ✅ Customize branding/colors in `ui/theme/Theme.kt`
3. ✅ Update app name in `AndroidManifest.xml`
4. ✅ Build release APK
5. ✅ Upload to Google Play Store

For detailed instructions, see `BUILD_INSTRUCTIONS.md`
