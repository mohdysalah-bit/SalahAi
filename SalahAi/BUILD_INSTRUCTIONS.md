# SalahAi Android Application - Build Instructions

This document provides step-by-step instructions to build the SalahAi Android application APK on your local machine.

## Prerequisites

Before you start, ensure you have the following installed:

1. **Java Development Kit (JDK) 17 or later**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`

2. **Android Studio (Latest version)**
   - Download from: https://developer.android.com/studio
   - Includes Android SDK and build tools

3. **Android SDK**
   - API Level 34 (Android 14) or higher
   - Build Tools 34.0.0 or higher
   - Install via Android Studio's SDK Manager

4. **Git** (optional, for version control)
   - Download from: https://git-scm.com/

## Setup Steps

### Step 1: Prepare Your Machine

1. **Install JDK 17**
   ```bash
   # macOS (using Homebrew)
   brew install openjdk@17
   
   # Ubuntu/Debian
   sudo apt-get install openjdk-17-jdk
   
   # Windows
   # Download from Oracle website and run installer
   ```

2. **Set JAVA_HOME environment variable**
   ```bash
   # macOS/Linux
   export JAVA_HOME=$(/usr/libexec/java_home -v 17)
   
   # Windows (PowerShell)
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
   ```

3. **Install Android Studio**
   - Download and install from https://developer.android.com/studio
   - Launch Android Studio and complete the setup wizard

### Step 2: Configure Android SDK

1. **Open Android Studio**
2. **Go to: Tools → SDK Manager**
3. **Install the following:**
   - Android SDK Platform 34
   - Android SDK Build-Tools 34.0.0
   - Android Emulator (optional, for testing)
   - Android SDK Platform-Tools

4. **Note the SDK location** (usually `~/Android/sdk` on macOS/Linux or `C:\Users\YourUsername\AppData\Local\Android\sdk` on Windows)

### Step 3: Clone/Download the Project

1. **Option A: Using Git**
   ```bash
   git clone <repository-url>
   cd SalahAi
   ```

2. **Option B: Download ZIP**
   - Download the project as ZIP
   - Extract to your desired location
   - Open terminal/command prompt in the extracted folder

### Step 4: Configure Backend URL

1. **Open the file:**
   ```
   app/src/main/kotlin/com/salahtech/salahAi/data/api/ApiConfig.kt
   ```

2. **Update the BASE_URL:**
   ```kotlin
   const val BASE_URL = "https://your-backend-url.com/"
   ```
   
   Replace with your actual backend URL (e.g., `https://aichatbot-atctaywz.manus.space/`)

### Step 5: Build the Debug APK

#### Using Android Studio (Recommended for beginners)

1. **Open the project in Android Studio**
   - File → Open → Select the SalahAi folder

2. **Wait for Gradle sync to complete**
   - Android Studio will automatically download dependencies

3. **Build the APK**
   - Go to: Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Wait for the build to complete

4. **Locate the APK**
   - The APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

#### Using Command Line

1. **Navigate to project directory**
   ```bash
   cd /path/to/SalahAi
   ```

2. **Build debug APK**
   ```bash
   ./gradlew assembleDebug
   ```
   
   On Windows:
   ```cmd
   gradlew.bat assembleDebug
   ```

3. **Locate the APK**
   ```bash
   app/build/outputs/apk/debug/app-debug.apk
   ```

### Step 6: Build the Release APK

#### Step 6a: Create a Signing Key (One-time setup)

1. **Using Android Studio:**
   - Go to: Build → Generate Signed Bundle / APK
   - Click "Create new..."
   - Fill in the details:
     - Key store path: Choose a location (e.g., `~/salahAi.keystore`)
     - Password: Create a strong password
     - Key alias: `salahAi`
     - Key password: Same as keystore password
   - Click "Create"

2. **Using Command Line:**
   ```bash
   keytool -genkey -v -keystore ~/salahAi.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias salahAi
   ```
   
   You'll be prompted to enter:
   - Keystore password
   - Key password
   - Your name, organization, etc.

#### Step 6b: Build Signed Release APK

1. **Using Android Studio:**
   - Go to: Build → Generate Signed Bundle / APK
   - Select "APK"
   - Choose your keystore file
   - Enter passwords
   - Select "release" build variant
   - Click "Create"

2. **Using Command Line:**
   
   First, update `app/build.gradle.kts` with signing config:
   ```kotlin
   android {
       ...
       signingConfigs {
           release {
               storeFile = file(System.getenv("HOME") + "/salahAi.keystore")
               storePassword = "your-keystore-password"
               keyAlias = "salahAi"
               keyPassword = "your-key-password"
           }
       }
       
       buildTypes {
           release {
               signingConfig = signingConfigs.getByName("release")
               isMinifyEnabled = true
               proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
           }
       }
   }
   ```
   
   Then build:
   ```bash
   ./gradlew assembleRelease
   ```

3. **Locate the APK**
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

## Testing the APK

### Option 1: Install on Physical Device

1. **Enable Developer Mode**
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   - Go to Settings → Developer Options
   - Enable "USB Debugging"

2. **Connect to Computer**
   - Connect your Android device via USB cable
   - Accept the USB debugging prompt on your phone

3. **Install APK**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```
   
   Or using Android Studio:
   - Run → Run 'app'
   - Select your device

### Option 2: Install on Emulator

1. **Create/Select Emulator**
   - Tools → AVD Manager
   - Create or select an emulator
   - Start the emulator

2. **Install APK**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```
   
   Or using Android Studio:
   - Run → Run 'app'
   - Select your emulator

## Troubleshooting

### Build Fails with Gradle Sync Error

**Solution:**
```bash
./gradlew clean
./gradlew build
```

Or in Android Studio:
- File → Sync Now
- File → Invalidate Caches → Invalidate and Restart

### "ANDROID_SDK_ROOT not set" Error

**Solution:**
```bash
# macOS/Linux
export ANDROID_SDK_ROOT=~/Android/sdk

# Windows (PowerShell)
$env:ANDROID_SDK_ROOT = "$env:LOCALAPPDATA\Android\sdk"
```

### Compilation Fails with Kotlin Error

**Solution:**
- Ensure Kotlin plugin is up to date in Android Studio
- Go to: Android Studio → Preferences → Plugins → Kotlin
- Update if available

### APK Installation Fails

**Solution:**
- Uninstall previous version: `adb uninstall com.salahtech.salahAi`
- Ensure device has enough storage
- Try on a different device/emulator

### API Connection Issues

**Solution:**
- Verify backend URL is correct in `ApiConfig.kt`
- Check network connectivity
- Ensure backend server is running
- Check firewall/proxy settings

## Building for Google Play Store

### Step 1: Create Google Play Developer Account
- Go to: https://play.google.com/console
- Sign up for a developer account ($25 one-time fee)

### Step 2: Create App Listing
- Create a new application
- Fill in app details:
  - App name: "SalahAi"
  - Default language: English
  - App category: Productivity or Communication

### Step 3: Prepare Store Listing
- Add app icon (512x512 PNG)
- Add screenshots (minimum 2)
- Write app description
- Add privacy policy URL

### Step 4: Upload APK
- Go to: Release → Production
- Upload your signed release APK
- Review and publish

### Step 5: Monitor Release
- Check Play Console for:
  - Installation metrics
  - Crash reports
  - User reviews
  - Performance data

## Advanced Build Options

### Build Variants

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Build all variants
./gradlew assemble
```

### Custom Build Properties

Create `local.properties` in project root:
```properties
sdk.dir=/path/to/android/sdk
ndk.dir=/path/to/android/ndk
```

### ProGuard/R8 Configuration

Edit `app/proguard-rules.pro` to customize code obfuscation for release builds.

## Performance Optimization

For release builds, the following are automatically enabled:
- Code minification (R8)
- Resource shrinking
- ProGuard rules
- Optimization

These reduce APK size and improve performance.

## Support and Resources

- **Android Documentation**: https://developer.android.com/docs
- **Gradle Documentation**: https://gradle.org/
- **Kotlin Documentation**: https://kotlinlang.org/docs/
- **Jetpack Compose**: https://developer.android.com/jetpack/compose

## Next Steps

1. Build the debug APK and test on your device
2. Fix any issues or customize the app
3. Build the release APK
4. Upload to Google Play Store
5. Monitor performance and user feedback

---

**Questions?** Refer to the README.md for more information about the project structure and features.
