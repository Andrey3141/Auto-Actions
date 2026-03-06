# PROJECT_STRUCTURE.md

# 📁 Структура проекта

Information/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/printer/information/
│   │   │   │   └── MainActivity.kt              # JNI мост (тонкий слой Kotlin)
│   │   │   ├── cpp/
│   │   │   │   ├── CMakeLists.txt                # Сборка C++ кода
│   │   │   │   ├── main.cpp                      # Главный JNI вход
│   │   │   │   ├── phone_formatter.cpp/.h        # Форматирование номеров (+7, +375)
│   │   │   │   ├── telegram_helper.cpp/.h        # Отправка в Telegram
│   │   │   │   ├── sms_helper.cpp/.h             # Отправка SMS
│   │   │   │   └── call_helper.cpp/.h            # Совершение звонков
│   │   │   ├── res/                               # Ресурсы (иконки, layout)
│   │   │   │   ├── drawable/                      # Векторные иконки (WebP)
│   │   │   │   ├── layout/
│   │   │   │   │   └── activity_main.xml         # Главный экран
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml                # Цветовая палитра
│   │   │   │   │   ├── strings.xml               # Тексты
│   │   │   │   │   └── themes.xml                # Тема приложения
│   │   │   │   └── mipmap-*/                      # Иконки приложения
│   │   │   └── AndroidManifest.xml                # Манифест
│   │   └── assets/                                 # Документация
│   │       ├── PROJECT_STRUCTURE.md
│   │       └── CHANGELOG.md
│   ├── build.gradle.kts                            # Конфигурация модуля
│   ├── proguard-rules.pro                          # Правила обфускации
│   └── release-key.jks                             # Ключ подписи (создаётся локально)
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties               # Версия Gradle
├── build.gradle.kts                                 # Корневая конфигурация
├── settings.gradle.kts                              # Настройки проекта
├── gradle.properties                                # Свойства Gradle
├── gradlew                                          # Скрипт сборки (Linux/Mac)
├── gradlew.bat                                      # Скрипт сборки (Windows)
└── local.properties                                 # Локальные пути (SDK)