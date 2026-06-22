# 🌍 Travel Planner App

> Android Course Project — **Advanced Computer Systems Engineering Laboratory (ENCS5150)**
> Birzeit University · First Semester 2026

A modern Android application for browsing, planning, and managing trips. The app fetches travel data from a RESTful API, stores it locally, and lets users register, reserve trips, save favorites, and manage their profile — all following Material Design and a clean **MVVM** architecture.

---

## 📑 Table of Contents

- [About](#about)
- [Team](#team)
- [Project Configuration](#project-configuration)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Admin Account](#admin-account)
- [Technical Requirements Checklist](#technical-requirements-checklist)
- [Academic Note](#academic-note)

---

## About

This is a group-based course project worth **100 marks**. The application is designed and implemented natively in **Java** for Android. Users can manage trips, reservations, favorites, and personal profiles in a modern, user-friendly UI.

The application type (**Travel Planner**) and theme (**Right Theme**) were assigned based on the team members' university IDs as defined in the project specification.

---

## Team

| Name | University ID |
|------|---------------|
| Anwar Atawneh | 1222275 |
| _Partner_ | 1220495 |

**Supervised by:** Advanced Computer Systems Engineering Lab

---

## Project Configuration

| Setting | Value | Rule |
|---------|-------|------|
| **App Type** | Travel Planner App | Smallest ID `1220495` → last digit `5` (odd) |
| **Theme** | Right Theme | Sum of IDs ends in `0` → `< 5` |

### 🎨 Color Palette (Right Theme)

| Color | Hex |
|-------|-----|
| Background / Light | `#EEEEEE` |
| Accent Green | `#6FCF97` |
| Primary | `#2FA084` |
| Primary Dark | `#1F6F5F` |

---

## Features

- 🚀 **Animated Splash Screen** — displays the app logo, then an introduction layout with a *Connect* button.
- 🌐 **REST API Integration** — fetches at least 10 trips and caches them in the local database.
- 🔐 **Authentication** — Login & Registration with full input validation, encrypted passwords, and *Remember Me* via Shared Preferences.
- 🧭 **Navigation Drawer** — central navigation across all sections.
- 🏝️ **Trips Section** — RecyclerView list with **search**, **filtering**, **favorite**, and **reservation/join** actions.
- 📝 **Reservations** — book trips through a form and view all current/past reservations.
- ⭐ **Favorites** — save trips, open details, or reserve directly.
- ✨ **Special Section** — Travel Offers, Popular Destinations, Recommended Trips.
- 👤 **Profile Management** — view and update personal information & profile picture.
- ☎️ **Contact Us** — Call, Locate (Maps), and Email via system intents.
- 🛠️ **Admin Panel** — separate drawer for managing users, trips, and reservations (full CRUD).

---

## Tech Stack

| Layer | Library |
|-------|---------|
| Language | Java |
| Networking | Retrofit 2 + Gson Converter |
| Image Loading | Glide |
| Local Database | Room |
| Architecture Components | ViewModel + LiveData |
| UI | Material Components, RecyclerView, Fragments, ConstraintLayout |
| Min SDK | API 24 (Android 7.0) |

---

## Architecture

The app follows the **MVVM (Model–View–ViewModel)** pattern:

```
UI (Activity / Fragment)
        │  observes LiveData
        ▼
   ViewModel
        │  calls
        ▼
  Repository  ──►  Remote (Retrofit / ApiService)
        │
        └──────►  Local (Room Database)
```

The **Repository** is the single source of truth — it decides whether to serve data from the network or the local cache.

---

## Project Structure

```
com.example.a1222275_anwaratawn_project
│
├── data
│   ├── model         → Trip, User, Reservation, Favorite
│   ├── remote        → RetrofitClient, ApiService
│   ├── local         → AppDatabase, DAOs
│   └── repository     → DataRepository
│
├── ui
│   ├── splash
│   ├── auth
│   ├── home          → MainActivity + Navigation Drawer
│   ├── trips
│   ├── reservations
│   ├── favorites
│   ├── profile
│   ├── contact
│   └── admin
│
├── viewmodel
│
└── utils             → Constants, PrefManager, SecurityUtils
```

---

## Getting Started

### Prerequisites

- Android Studio (latest stable)
- JDK 17
- An Android device or emulator (API 24+)

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/AnwarRadwan/Project-Android-Course-Project.git

# 2. Open the project in Android Studio
#    File ▸ Open ▸ select the project folder

# 3. Let Gradle sync, then configure the API base URL
```

In `data/remote/RetrofitClient.java`, set your API endpoint:

```java
private static final String BASE_URL = "https://your-api-url.com/";
```

Then **Build ▸ Run** ▶️ on your device or emulator.

---

## Admin Account

A pre-created admin account is included with a separate navigation drawer:

```
Email:    admin@admin.com
Password: Admin123!
```

Admin capabilities: add/view/delete users, add admins, add/edit/delete trips, and view reservations.

---

## Technical Requirements Checklist

- [x] Android Layouts (Static & Dynamic)
- [x] Navigation Drawer
- [x] RecyclerView
- [x] Fragments
- [x] Intents
- [x] Notifications (Toast / Push)
- [x] Shared Preferences
- [x] Database Management
- [x] RESTful API Integration
- [x] Animations (at least 2)
- [x] Input Validation
- [x] Exception Handling

---

## Academic Note

This project was developed for educational purposes as part of the **ENCS5150** course at **Birzeit University**. It is intended for academic submission and learning.

---

<p align="center">Made with ☕ and Java · Birzeit University 2026</p>
