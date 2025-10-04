# TouroCraft

TouroCraft is an Android application built with **Java** that helps users plan trips effortlessly. By entering details like budget, source, destination, preferred transport, and travel date, TouroCraft recommends the best and most affordable travel and stay options.

---

## Features

* Login Screen – Secure user authentication to personalize the experience
* Smart Trip Planner – Suggests travel plans based on budget and preferences
* Transport Options – Supports multiple modes of transportation:

  * Train
  * Flight
  * Bus
* Hotel Recommendations – Provides affordable and suitable stay options
* Budget-Friendly Suggestions – Optimized recommendations tailored to the user's budget
* Bing API Integration – Uses Microsoft Bing API to fetch recommendations and relevant travel information

---

## Screens / Flow

1. Login Screen – Authenticate users before accessing trip planning
2. Trip Input Screen – Collects user data:

   * Budget
   * Source Place
   * Destination
   * Mode of Transport (train, flight, bus)
   * Date of Travel
3. Recommendations Screen – Displays best travel and stay suggestions powered by Bing API

---

## Tech Stack

* Language: Java
* Platform: Android
* Database: (Add if using SQLite, Firebase, or any backend service)
* UI: XML layouts with Material Design components
* API: Microsoft Bing API (for travel recommendations and search data)

---

## Getting Started

### Prerequisites

* Android Studio (latest version)
* Java 8 or above
* Android SDK
* Microsoft Bing API key

### Installation

1. Clone this repository
2. Open the project in **Android Studio**
3. Add your **Bing API Key** in the configuration file or constants (replace placeholder with your key).
4. Build and run on an emulator or Android device

---

## Project Structure (Example)

```
TouroCraft/
 ├── app/
 │   ├── src/
 │   │   ├── main/
 │   │   │   ├── java/com/tourocraft/...   # Java source files
 │   │   │   ├── res/                      # Layouts, drawables, etc.
 │   │   │   └── AndroidManifest.xml
 └── build.gradle
```

---
## Acknowledgements

* Microsoft Bing API for recommendation and data integration
* Android Studio and Java for development
* Material Design for UI components
