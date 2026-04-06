<div align="center">
  
# 🇧🇾 Auto-Actions v1.1.2

![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple?logo=kotlin)
![Android](https://img.shields.io/badge/Android-8.0+-green?logo=android)
![NDK](https://img.shields.io/badge/NDK-C/C++-blue?logo=c)
![License](https://img.shields.io/badge/License-MIT-green.svg)
![Status](https://img.shields.io/badge/Status-Beta-green)
![Size](https://img.shields.io/badge/Size-2.93MB-blue)

> ⚡ Утилита для быстрой отправки геолокации, SMS и звонков на белорусские номера  
> 🧠 Native C-оптимизация • 🔢 Валидация +375 • 📱 Кастомный UI

</div>

---

## 📋 Оглавление
- [📖 О проекте](#-о-проекте)
- [✨ Возможности](#-возможности)
- [📸 Демонстрация](#-демонстрация)
- [📥 Установка](#-установка)
- [🔨 Сборка из исходников](#-сборка-из-исходников)
- [🎮 Использование](#-использование)
- [🛠 Технологии](#-технологии)
- [🧱 Архитектура](#-архитектура)
- [🔧 Решение проблем](#-решение-проблем)
- [📄 Лицензия](#-лицензия)
- [📬 Контакты](#-контакты)

---

## 📖 О проекте

**Auto-Actions** — это легковесное Android-приложение для мгновенного выполнения действий с телефонными номерами Республики Беларусь.

> 🎯 **Основная задача:** Ввести номер → одним тапом отправить геолокацию в Telegram, отправить SMS или позвонить.

### 🔥 Ключевая особенность
Вся «тяжёлая» логика (форматирование номеров, подготовка данных) вынесена в **нативный слой на C (NDK)**. Это обеспечивает:
- ✅ Мгновенную валидацию и форматирование без задержек UI
- ✅ Минимальное потребление памяти
- ✅ Оптимизацию бинарника (`-Os`, `LTO`, `--gc-sections`)

---

## ✨ Возможности

| Фича | Описание |
|------|----------|
| 🔢 **Валидация BY-номеров** | Строгая проверка форматов `+375`, `80`, `375` с визуальной индикацией |
| 📍 **Геолокация в Telegram** | Мгновенная отправка координат через Deep Link (`tg://msg?text=...`) |
| 💬 **Быстрые SMS** | Отправка шаблона сообщения в один клик |
| 📞 **Умный дозвон** | Автоматический выбор установленного телефонного приложения (Dialer) |
| ⚡ **Native C-ядро** | Форматирование и логика на C для максимальной производительности |
| 🎨 **Анимированный UI** | Плавные переходы, пульсация кнопок, полноэкранный режим |

---

## 📸 Демонстрация

<div align="center">

| 🏠 Главный экран |
|:--------------:|
| ![Main](screenshots/main_window.jpg) |
| *Визуал приложения* |

</div>

---

## 📥 Установка

### ⚡ Готовый APK

```bash
# 1. Скачайте последнюю версию
[![Download APK](https://img.shields.io/badge/⬇️_Скачать-.apk-0078D4?style=for-the-badge&logo=android)](https://github.com/Andrey3141/Auto-Actions/releases/latest/download/Auto-Actions.apk)

# 2. Установите файл
Разрешите "Установку из неизвестных источников" при первом запуске.
```

### 📦 Требования
| Компонент | Версия |
|-----------|--------|
| 📱 Android | 8.0+ (API 26) |
| 🌐 Интернет | Требуется для Telegram/СМС |
| 🔐 Разрешения | Location, SMS, Phone |

---

## 🔨 Сборка из исходников

### 🛠 Предварительные требования

```bash
✓ Android Studio Giraffe+
✓ JDK 17+
✓ Android NDK 25+ (для C-модулей)
✓ CMake 3.22+
```

### 🚀 Пошаговая сборка

```bash
# 1. Клонируйте репозиторий
git clone https://github.com/Andrey3141/Auto-Actions.git
cd Auto-Actions

# 2. Откройте в Android Studio
#    Проект автоматически подгрузит CMake и NDK

# 3. Соберите нативные библиотеки
./gradlew build

# 4. Установите на устройство
./gradlew installDebug
```

### ⚙️ Флаги компиляции (CMake)

В проекте используются агрессивные флаги оптимизации (`CMakeLists.txt`):

```cmake
# Оптимизация размера и скорости
-OS -fvisibility=hidden -fdata-sections -ffunction-sections -flto

# Линковка: удаление неиспользуемого кода
-Wl,--gc-sections -Wl,-s -Wl,--icf=all
```

---

## 🎮 Использование

### 1️⃣ Ввод номера
- Приложение принимает форматы: `+375`, `80`, `375`.
- **Нативная функция** `formatPhoneNumberNative` автоматически форматирует ввод.
- Зеленая галочка ✅ подтверждает корректность номера.

### 2️⃣ Выбор действия

| Кнопка | Действие |
|--------|----------|
| 📍 **Геолокация** | Получает GPS-координаты → открывает Telegram с сообщением: *"📍 Моя геолокация: [ссылка на карту]"* |
| 💬 **SMS** | Отправляет шаблонное сообщение через `SmsManager` |
| 📞 **Звонок** | Запускает `Intent.ACTION_CALL`, автоматически подбирая нужный диалер |

### 🔐 Разрешения
При первом нажатии приложение запросит:
- 📍 `ACCESS_FINE_LOCATION` — для отправки координат
- 💬 `SEND_SMS` — для отправки сообщений
- 📞 `CALL_PHONE` — для совершения звонков

---

## 🛠 Технологии

```
📱 Kotlin           — UI и логика Android (Activity, Permissions, Intents)
⚙️ C / JNI          — высокопроизводительная обработка строк и утилиты
🔗 Telegram Deep Link — отправка геолокации без использования Bot API
🎨 Кастомный UI — карточки, анимации, адаптивная верстка (без Material Components)
🔧 CMake / NDK      — сборка нативных библиотек
```

### 📊 Распределение кода

| Язык | Роль |
|------|------|
| 🟣 **Kotlin** | UI, жизненный цикл, работа с Android API, анимации |
| 🔵 **C** | Форматирование номеров, подготовка данных, системные вызовы |

---

## 🧱 Архитектура

```
app/
├── src/main/
│   ├── java/com/printer/information/
│   │   ├── MainActivity.kt    # Точка входа: UI + логика разрешений
│   │   └── ...
│   ├── cpp/                   # Native Layer (NDK)
│   │   ├── CMakeLists.txt     # Конфигурация сборки
│   │   ├── main.c             # JNI Bridge (связь Kotlin ↔ C)
│   │   ├── phone_formatter.c  # Логика форматирования +375
│   │   ├── telegram_helper.c  # Формирование ссылок для Telegram
│   │   ├── sms_helper.c       # Утилиты для SMS
│   │   └── call_helper.c      # Логика звонков
│   └── res/                   # Layouts, Drawables, Animations
```

### 🔗 JNI Интерфейс

```c
// main.c — мост между Kotlin и C
JNIEXPORT jstring JNICALL Java_..._formatPhoneNumberNative(...);
JNIEXPORT jboolean JNICALL Java_..._sendLocationNative(...);
JNIEXPORT jboolean JNICALL Java_..._sendSmsNative(...);
JNIEXPORT jboolean JNICALL Java_..._makeCallNative(...);
```

---

## 🔧 Решение проблем

<details>
<summary>❌ Ошибка: «NDK not found» при сборке</summary>

1.  Открой **SDK Manager** → **SDK Tools**
2.  Установи **NDK (Side by side)** версии 25+
3.  В `local.properties` укажи путь: `ndk.dir=/path/to/ndk`
</details>

<details>
<summary>❌ Telegram не открывается при отправке локации</summary>

Приложение использует **Deep Links** (`tg://`). Убедитесь, что:
1.  Установлен официальный клиент Telegram
2.  В `AndroidManifest.xml` разрешены соответствующие `queries` (Android 11+)
</details>

<details>
<summary>❌ Номер не проходит валидацию</summary>

Приложение заточено под **Беларусь**. Поддерживаются только форматы:
- `+375...`
- `80...`
- `375...`
</details>

---

## 🤝 Вклад в проект

1.  Форкни репозиторий
2.  Создай ветку: `git checkout -b feat/native-optimization`
3.  Вноси изменения в C-код или Kotlin UI
4.  Проверь сборку: `./gradlew assembleDebug`
5.  Открой Pull Request

---

## 📄 Лицензия

<div align="center">

[![MIT License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Проект распространяется под лицензией **MIT**.
</div>

---

<div align="center">

## 📬 Контакты

> 💬 Вопросы по JNI, NDK или архитектуре? Пиши!

[![GitHub](https://img.shields.io/badge/GitHub-Profile-181717?logo=github&style=for-the-badge)](https://github.com/Andrey3141)
[![Telegram](https://img.shields.io/badge/Telegram-@tools271-2CA5E0?logo=telegram&style=for-the-badge)](https://t.me/tools271)

</div>

---

<div align="center">

## 🚀 Скачать

[![Download APK](https://img.shields.io/badge/⬇️_Скачать_Авто-Действия-.apk-3DDC84?style=for-the-badge&logo=android)](https://github.com/Andrey3141/Auto-Actions/releases)

---

**Auto-Actions** — скорость нативного кода, простота одного тапа. 🇧🇾⚡

*Сделано с ❤️ на Kotlin + C (NDK)*

</div>
