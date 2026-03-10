# Flutter Screen Swipe App - Cviko 5

**Student:** Dominik Sabota  
**Course:** XPC-MMA (Cross-Platform Mobile Applications)  
**VUT FIT:** 2025/2026  
**Points:** 10/10

---

## 📱 Project Overview

A Flutter mobile application demonstrating screen navigation with swipe gestures, state management, and toast notifications.

### Features Implemented

✅ **Screen Swiping (3 points)**
- PageView with smooth horizontal swipe transitions
- Visual page indicator (dots)
- Animated transitions between screens

✅ **Toast Notifications (3 points)**
- Shows current page number on swipe
- Uses Fluttertoast package
- Custom styling (purple background)

✅ **Data Transfer Between Screens (4 points)**
- Text input on Screen 1
- Send button transfers data to Screen 2
- Input field clears after sending
- Data displays on Screen 2

✅ **Code Organization**
- Separate files: `main.dart`, `first_screen.dart`, `second_screen.dart`
- Clean architecture with proper state management

**Total:** 10/10 points ✅

---

## 🛠️ Technology Stack

- **Framework:** Flutter 3.38.9
- **Language:** Dart
- **Platform:** Android (tested on Medium Phone API 36.1, Android 16.0 Baklava)
- **External Package:** fluttertoast ^8.2.14

---

## 📂 Project Structure

```
flutter_screen_swipe_app/
├── lib/
│   ├── main.dart                 # Entry point, PageView, state management
│   └── screens/
│       ├── first_screen.dart     # Text input screen
│       └── second_screen.dart    # Display screen
├── pubspec.yaml                  # Dependencies
├── android/                      # Android configuration
└── ios/                          # iOS configuration
```

---

## 🚀 Setup Instructions

### Prerequisites

1. **Flutter SDK** installed
   - Download from: https://flutter.dev/docs/get-started/install
   - Verify: `flutter doctor`

2. **Android Studio** (for Android development)
   - Android SDK Command-line Tools
   - Android Emulator or physical device

3. **VS Code** (recommended) with Flutter extensions

---

### Installation Steps

#### 1. Navigate to Project Directory

```bash
cd C:\Users\domin\Desktop\MMA\XPC-MMA-2025\flutter_screen_swipe_app
```

#### 2. Install Dependencies

```bash
flutter pub get
```

**Expected output:**
```
Running "flutter pub get" in flutter_screen_swipe_app...
Resolving dependencies...
+ fluttertoast 8.2.14
Got dependencies!
```

#### 3. Verify Flutter Installation

```bash
flutter doctor
```

**Ensure:**
- ✅ Flutter SDK installed
- ✅ Android toolchain complete
- ✅ Android Studio with SDK tools

#### 4. Fix Android Licenses (if needed)

```bash
flutter doctor --android-licenses
```

Press `y` to accept all licenses.

---

### Running the App

#### Option 1: Using Command Line

```bash
# Start emulator first (or connect physical device)
# Then run:
flutter run
```

**First build:** ~20 minutes (downloads dependencies)  
**Subsequent builds:** ~30 seconds

#### Option 2: Using VS Code

1. Open project in VS Code
2. Press `F5` or click "Run" → "Start Debugging"
3. Select device from device picker

#### Option 3: Using Android Studio

1. Open project in Android Studio
2. Select device/emulator
3. Click Run button (▶️)

---

### Hot Reload During Development

While app is running:
- **Hot Reload:** Press `r` in terminal (updates UI instantly)
- **Hot Restart:** Press `R` in terminal (full restart)
- **Quit:** Press `q`

---

## 💡 How to Use the App

### Screen 1 (Input)
1. Enter text in the text field
2. Click "Send to Screen 2" button
3. Text field clears
4. Swipe left to see Screen 2

### Screen 2 (Display)
1. View received text from Screen 1
2. Swipe right to return to Screen 1

### Toast Notifications
- Swipe between screens
- Toast appears at bottom showing "Current Page: 1" or "Current Page: 2"

---

## 📝 Code Explanation

### main.dart - Core Logic

**State Management:**
```dart
String _sharedText = '';  // Shared data between screens

void updateSharedText(String text) {
  setState(() {
    _sharedText = text;
  });
}
```

**PageView Controller:**
```dart
PageController _pageController = PageController();

PageView(
  controller: _pageController,
  onPageChanged: (index) {
    setState(() {
      _currentPage = index;
    });
    _showPageToast(index);
  },
  children: [
    FirstScreen(...),
    SecondScreen(receivedText: _sharedText),
  ],
)
```

**Toast Notification:**
```dart
void _showPageToast(int pageNumber) {
  Fluttertoast.showToast(
    msg: "Current Page: ${pageNumber + 1}",
    toastLength: Toast.LENGTH_SHORT,
    gravity: ToastGravity.BOTTOM,
    backgroundColor: Colors.deepPurple,
    textColor: Colors.white,
  );
}
```

---

### first_screen.dart - Input Screen

**Text Controller:**
```dart
final TextEditingController _textController = TextEditingController();

void _handleSendButton() {
  final text = _textController.text.trim();
  
  if (text.isNotEmpty) {
    widget.onSendText(text);        // Send to parent
    _textController.clear();         // Clear input
    widget.onNavigateToSecond();    // Navigate to Screen 2
  }
}
```

**Callbacks:**
- `Function(String) onSendText` - Sends text to parent
- `VoidCallback onNavigateToSecond` - Triggers navigation

---

### second_screen.dart - Display Screen

**Conditional Rendering:**
```dart
Text(
  widget.receivedText.isEmpty
    ? 'No text received yet. Go to Screen 1 and send some text!'
    : widget.receivedText,
  style: TextStyle(
    color: widget.receivedText.isEmpty ? Colors.grey : Colors.black,
    fontStyle: widget.receivedText.isEmpty ? FontStyle.italic : FontStyle.normal,
  ),
)
```

---

## 🔧 Key Concepts Used

### 1. StatefulWidget vs StatelessWidget
- **StatefulWidget** (main.dart, first_screen.dart): Can change over time
- **StatelessWidget** (second_screen.dart): Immutable

### 2. State Lifting
- Shared state (`_sharedText`) lives in parent (`MyHomePage`)
- Child widgets modify state via callbacks

### 3. Callback Functions
- `Function(String) onSendText` - Passes data up
- `VoidCallback onNavigateToSecond` - Triggers action

### 4. Lifecycle Management
```dart
@override
void dispose() {
  _pageController.dispose();
  _textController.dispose();
  super.dispose();
}
```

---

## 📦 Dependencies

### pubspec.yaml

```yaml
dependencies:
  flutter:
    sdk: flutter
  cupertino_icons: ^1.0.8
  fluttertoast: ^8.2.14      # Toast notifications
```

---

## 🐛 Troubleshooting

### Issue: "flutter: command not found"
**Solution:** Add Flutter to PATH
```bash
# Windows
set PATH=%PATH%;C:\flutter\bin

# Permanently: Edit System Environment Variables
```

### Issue: "Gradle build failed"
**Solution:** 
```bash
cd android
./gradlew clean
cd ..
flutter clean
flutter pub get
flutter run
```

### Issue: "Android licenses not accepted"
**Solution:**
```bash
flutter doctor --android-licenses
```
Press `y` for all.

### Issue: Hot reload not working
**Solution:** Use Hot Restart (`R`) instead of Hot Reload (`r`)

---

## 📸 Screenshots

### Screen 1 - Input
- Text input field
- Send button
- Page indicator at bottom

### Screen 2 - Display
- Received text display
- Success indicator
- Page indicator at bottom

### Toast Notification
- Appears on swipe
- Shows current page number
- Purple background, white text

---

## 🎓 Learning Outcomes

### Skills Demonstrated
1. ✅ Flutter widget composition
2. ✅ State management patterns
3. ✅ Navigation (PageView)
4. ✅ External package integration
5. ✅ Callback functions
6. ✅ Conditional rendering
7. ✅ Memory management (dispose)

### Flutter Concepts Mastered
- StatefulWidget lifecycle
- TextEditingController
- PageController
- setState() usage
- Widget tree structure
- Hot reload workflow

---

## 📊 Performance Notes

**First Build:**
- Time: ~20 minutes (~1185 seconds)
- Downloads: NDK, Build-Tools, CMake
- Normal for first Android build

**Subsequent Builds:**
- Time: ~30 seconds
- Hot reload: <1 second

---

## 🔄 Git Workflow

```bash
# Stage changes
git add flutter_screen_swipe_app

# Commit
git commit -m "Complete Flutter screen swipe app - Cviko5 (10pts)"

# Push
git push origin main
```

---

## 📚 Resources

- **Flutter Documentation:** https://flutter.dev/docs
- **Fluttertoast Package:** https://pub.dev/packages/fluttertoast
- **PageView Widget:** https://api.flutter.dev/flutter/widgets/PageView-class.html
- **State Management:** https://flutter.dev/docs/development/data-and-backend/state-mgmt

---

## ✅ Assignment Checklist

- [x] Screen swiping with PageView (3 points)
- [x] Toast notifications on swipe (3 points)
- [x] Text input on Screen 1 (4 points)
- [x] Data transfer to Screen 2 (4 points)
- [x] Input clears after send (4 points)
- [x] Separate code files (requirement)
- [x] Clean code with comments
- [x] App runs without errors

**Total: 10/10 points** ✅

---

## 👨‍💻 Author

**Dominik Sabota**  
VUT FIT - XPC-MMA 2025/2026  
GitHub: https://github.com/dmnkSabota/XPC-MMA-2025

---

## 📄 License

This project is part of VUT FIT coursework.

---

**Last Updated:** March 10, 2026  
**Flutter Version:** 3.38.9  
**Dart Version:** 3.x
