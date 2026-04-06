# PROJECT_STRUCTURE.md

# 📁 Структура проекта

Information/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/printer/information/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── MaterialTextInputReplica.kt
│   │   │   ├── cpp/
│   │   │   │   ├── CMakeLists.txt
│   │   │   │   ├── main.cpp
│   │   │   │   ├── phone_formatter.cpp/.h
│   │   │   │   ├── telegram_helper.cpp/.h
│   │   │   │   ├── sms_helper.cpp/.h
│   │   │   │   └── call_helper.cpp/.h
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   └── custom_text_input.xml
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── mipmap-*/
│   │   │   └── AndroidManifest.xml
│   │   └── assets/
│   │       ├── PROJECT_STRUCTURE.md
│   │       └── CHANGELOG.md
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── release-key.jks
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── local.properties
