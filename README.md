# SalahAi - Android Application

SalahAi is a native Android application that provides an elegant, feature-rich AI chat interface. It integrates with the backend AI chat API to deliver intelligent conversations with advanced capabilities including file attachments, web browsing, code execution, conversation memory, and custom instructions.

## Features

### Core Chat Features
- **Full-screen chat interface** with elegant message bubbles
- **Persistent conversation history** stored locally and synced with backend
- **Manus OAuth authentication** for secure user access
- **Conversation management** - create, rename, delete, and switch between chats
- **Real-time typing indicators** and loading states
- **Markdown rendering** for AI responses with syntax highlighting

### Advanced Features
1. **File Attachments** - Upload images, PDFs, and documents with S3 storage integration
2. **Direct Web Browsing** - Fetch and extract content from URLs
3. **Code Interpreter** - Execute Python code with LLM-based processing
4. **Conversation Memory** - Store and retrieve session-specific key-value pairs
5. **Custom Instructions** - Persistent user preferences for system prompts

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Repository pattern
- **Database**: Room with SQLite
- **Networking**: Retrofit with OkHttp
- **Serialization**: Kotlinx Serialization
- **Dependency Injection**: Hilt
- **Minimum SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 14 (API 34)

## Project Structure

```
SalahAi/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/salahtech/salahAi/
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/
│   │   │   │   ├── api/
│   │   │   │   │   ├── ApiClient.kt (API models and service)
│   │   │   │   │   └── ApiConfig.kt (Retrofit configuration)
│   │   │   │   ├── db/
│   │   │   │   │   └── SalahAiDatabase.kt (Room database)
│   │   │   │   └── repository/
│   │   │   │       └── ChatRepository.kt (Data access layer)
│   │   │   ├── viewmodel/
│   │   │   │   └── ChatViewModel.kt (UI state management)
│   │   │   └── ui/
│   │   │       ├── screens/
│   │   │       │   └── ChatScreen.kt (Main chat UI)
│   │   │       ├── components/
│   │   │       │   └── MessageBubble.kt (Reusable components)
│   │   │       └── theme/
│   │   │           ├── Theme.kt (Material 3 theme)
│   │   │           └── Type.kt (Typography)
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Setup Instructions

### Prerequisites
- Android Studio Flamingo or later
- Kotlin 1.9.0 or later
- JDK 17 or later
- Android SDK 34 (API level 34)

### Building the Project

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd SalahAi
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the SalahAi directory

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle files
   - Wait for the build to complete

4. **Configure Backend URL**
   - Update `BASE_URL` in `app/src/main/kotlin/com/salahtech/salahAi/data/api/ApiConfig.kt`
   - Set it to your backend API endpoint (e.g., `https://aichatbot-atctaywz.manus.space`)

5. **Build the Application**
   - Select "Build" > "Build Bundle(s) / APK(s)" > "Build APK(s)"
   - Or use the keyboard shortcut: `Ctrl+F9` (Windows/Linux) or `Cmd+F9` (Mac)

### Running on Emulator or Device

1. **Create or select an emulator**
   - Open AVD Manager in Android Studio
   - Create a new virtual device or select an existing one

2. **Run the application**
   - Click the "Run" button or press `Shift+F10`
   - Select your target device/emulator

## API Integration

The application communicates with the backend API at the configured base URL. All API calls are authenticated using session cookies obtained through OAuth login.

### Key API Endpoints
- `GET /api/trpc/auth.me` - Get current user
- `POST /api/trpc/auth.logout` - Logout user
- `GET /api/trpc/chat.listConversations` - List all conversations
- `POST /api/trpc/chat.createConversation` - Create new conversation
- `GET /api/trpc/chat.getMessages` - Get messages for conversation
- `POST /api/trpc/chat.sendMessage` - Send message
- `POST /api/trpc/advanced.*` - Advanced features (uploads, web browsing, etc.)

## Database Schema

The application uses Room database with the following entities:
- **conversations** - Chat conversations
- **messages** - Chat messages
- **attachments** - File attachments
- **conversation_memory** - Session-specific memory
- **user_preferences** - User settings and custom instructions

## Building Release APK

1. **Generate signing key** (if not already created)
   ```bash
   keytool -genkey -v -keystore salahAi-release.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias salahAi
   ```

2. **Configure signing in build.gradle.kts**
   ```kotlin
   signingConfigs {
       release {
           storeFile = file("path/to/salahAi-release.keystore")
           storePassword = "your-store-password"
           keyAlias = "salahAi"
           keyPassword = "your-key-password"
       }
   }
   ```

3. **Build signed APK**
   - Select "Build" > "Build Bundle(s) / APK(s)" > "Build APK(s)"
   - Or use Gradle command: `./gradlew assembleRelease`

4. **Locate the APK**
   - The signed APK will be in `app/build/outputs/apk/release/`

## Testing

Run unit tests with:
```bash
./gradlew test
```

Run instrumented tests on a device/emulator with:
```bash
./gradlew connectedAndroidTest
```

## Deployment

### Google Play Store
1. Create a Google Play Developer account
2. Generate a signed APK (see above)
3. Create a new app in Google Play Console
4. Upload the signed APK
5. Fill in app details, screenshots, and descriptions
6. Submit for review

### Direct Installation
1. Build a signed APK
2. Transfer the APK to your Android device
3. Enable "Unknown Sources" in Settings > Security
4. Open the APK file and install

## Troubleshooting

### Build Issues
- **Gradle sync fails**: Try "File" > "Sync Now" or clean build with `./gradlew clean build`
- **Kotlin compilation errors**: Ensure Kotlin plugin is up to date in Android Studio

### Runtime Issues
- **API connection fails**: Check backend URL configuration and network connectivity
- **Database errors**: Clear app data via Settings > Apps > SalahAi > Storage > Clear Data

### Performance Issues
- **Slow message loading**: Check database query optimization and consider pagination
- **High memory usage**: Enable ProGuard/R8 minification in release builds

## Future Enhancements

- Token-by-token streaming for real-time AI responses
- Drag-and-drop file uploads with paste support
- Automatic memory extraction from conversations
- Custom instruction presets and versioning
- Real sandboxed code execution environment
- Web browsing attribution and result caching
- Offline mode with sync when online
- Voice input and text-to-speech support
- Push notifications for new messages

## License

This project is proprietary and confidential.

## Support

For issues, questions, or feature requests, please contact the development team.
