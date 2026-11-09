# 📍 GeoSwitch – Smart Location & Time-Based Automation

**GeoSwitch** is an Android application that automates smartphone sound profiles and selected device settings based on geographical location and scheduled time.

Using GPS-based geofencing and Android alarm scheduling, GeoSwitch helps users automatically switch between Silent and Normal modes, manage Wi-Fi settings, and receive important notifications without manually changing their device settings.

The application is designed to reduce unwanted interruptions in classrooms, meetings, workplaces, and other environments where controlling smartphone notifications is important.

## 🚀 Key Features

### 📍 1. Location-Based Automation
- Integrates Google Maps to select geographical locations.
- Supports GPS-based geofencing.
- Allows users to define custom geofence locations and radius.
- Automatically switches the phone to Silent mode when entering a configured location.
- Restores Normal mode when leaving the configured location.

### ⏰ 2. Time-Based Automation
- Create custom sound-profile schedules.
- Configure separate start and end times.
- Uses Android AlarmManager to trigger scheduled actions.
- Automatically switches between Silent and Normal modes.

### 📶 3. Wi-Fi Automation
- Optional Wi-Fi automation setting.
- Automatically disables Wi-Fi when entering a configured geofence.
- Restores Wi-Fi when exiting the geofence.

### 🔔 4. Notification Management
- Schedule custom event notifications.
- Receive notifications for configured events.
- Includes missed-call monitoring functionality.
- Generates an important-call notification when repeated calls are detected from the same number.

### 🗺️ 5. Google Maps Integration
- Interactive Google Maps interface.
- Select locations using long-press interaction.
- Displays location markers and geofence radius.
- Supports location-based automation configuration.

## ⚙️ How It Works

GeoSwitch operates through two primary automation mechanisms:

**Location-Based Automation**

1. The user selects a geographical location through Google Maps.
2. A geofence is created using the selected coordinates and radius.
3. Android monitors geofence transitions in the background.
4. When the device enters the configured area, the application switches to Silent mode.
5. When the device exits the area, the application restores Normal mode.

**Time-Based Automation**

1. The user creates a schedule by selecting a name and start/end times.
2. The application stores the schedule in its SQLite database.
3. Android AlarmManager schedules the corresponding background events.
4. SilentReceiver and UnsilentReceiver handle the sound-profile changes.
5. The device automatically switches sound modes according to the configured schedule.

## 🛠️ Technologies & Tools Used

| Technology | Purpose |
|---|---|
| Java | Primary programming language |
| Android Studio | Android application development |
| Android SDK | Android system integration |
| Google Maps SDK | Interactive map integration |
| Google Play Services Location | GPS and geofencing |
| AlarmManager | Time-based scheduling |
| BroadcastReceiver | Background event handling |
| SQLite | Local database management |
| SharedPreferences | Persistent settings storage |
| XML | Android UI layouts and resources |
| Gradle | Build automation and dependency management |

## 🏗️ System Architecture

The application consists of several major components:

- **Main Activity:** Main navigation and application entry point.
- **Location Module:** Handles location selection and geofence configuration.
- **Geofence Module:** Detects entry and exit transitions.
- **Time Scheduling Module:** Creates and manages scheduled automation.
- **Notification Module:** Handles scheduled event notifications.
- **Background Receivers:** Execute sound-profile changes and respond to system events.
- **Database Module:** Stores location, time, and event information.

## 📂 Project Structure

```text
GeoSwitch/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/run/geoswitch/
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── MapsActivity.java
│   │   │   │   ├── GeofenceHelper.java
│   │   │   │   ├── GeofenceBroadcastReceiver.java
│   │   │   │   ├── SilentReceiver.java
│   │   │   │   ├── UnsilentReceiver.java
│   │   │   │   ├── time_home.java
│   │   │   │   ├── location_home.java
│   │   │   │   ├── notification.java
│   │   │   │   ├── services.java
│   │   │   │   ├── missedCalls.java
│   │   │   │   ├── TimeDatabaseHelper.java
│   │   │   │   ├── MapDatabaseHelper.java
│   │   │   │   └── eventDatabaseHelper.java
│   │   │   │
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   └── androidTest/
│   │
│   └── build.gradle
│
├── build.gradle
├── settings.gradle
├── gradle/
└── README.md
```

## 💻 Installation & Setup

### Prerequisites

- Android Studio
- JDK compatible with the configured Gradle version
- Android SDK
- Android device or emulator
- Google Maps API key

### 1. Clone the Repository

```bash
git clone https://github.com/ArunAnandOG/GeoSwitch.git
```

### 2. Open the Project

Open Android Studio and select the cloned GeoSwitch project.

### 3. Configure Google Maps API

Create a Google Maps API key through Google Cloud Console.

Add the key to your local `local.properties` file:

```properties
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
```

In `AndroidManifest.xml`, reference the property instead of hardcoding the key:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="${MAPS_API_KEY}" />
```

Ensure the Maps Secrets Gradle Plugin is configured correctly.

### 4. Sync Gradle

Allow Android Studio to download the required dependencies and synchronize the project.

### 5. Run the Application

Connect an Android device with USB debugging enabled or start an emulator.

Click **Run** in Android Studio to build and install the application.

## 📱 Android Configuration

| Configuration | Value |
|---|---|
| Application ID | `com.run.geoswitch` |
| Minimum SDK | Android 7.0 (API 24) |
| Target SDK | Android 13 (API 33) |
| Compile SDK | API 33 |
| Version | 1.0 |
| Language | Java |

## 🎯 Project Objectives

- Automate smartphone sound-profile management.
- Implement location-aware automation using GPS and geofencing.
- Schedule automatic device actions using Android AlarmManager.
- Reduce interruptions in classrooms, meetings, and workplaces.
- Explore Android background services, broadcast receivers, and system-level integrations.

## 🔮 Future Enhancements

- Introduce more customizable sound profiles.
- Add Bluetooth-based automation.
- Improve automation rule management.
- Add automation history and activity logs.
- Improve battery optimization and background execution.
- Enhance compatibility with newer Android versions.

## 🎓 Project Information

**Project Name:** GeoSwitch

**Platform:** Android

**Programming Language:** Java

**Development Environment:** Android Studio

---

Developed as an Android application development project.
