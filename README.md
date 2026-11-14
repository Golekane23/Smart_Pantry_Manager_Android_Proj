# Smart Pantry Manager

A Java Android application that stores pantry ingredients locally and suggests only recipes for which every required ingredient is present in sufficient quantity.

## Technology
- Java and Android Studio
- SQLite through `SQLiteOpenHelper`
- RecyclerView with custom adapters
- Android Intents for navigation

## Setup and run
1. Install Android Studio and the Android SDK for API 35.
2. Open this folder using **File > Open** in Android Studio.
3. Allow Gradle to sync and install the requested AndroidX dependencies.
4. Create an Android Virtual Device (AVD) or connect an Android phone with USB debugging enabled.
5. Press **Run** and select the device.
6. Add pantry ingredients, then open Suggested Recipes.

## Database
SQLite was selected because the application is a self-contained, offline pantry tool. It requires no server, network connection, or account and demonstrates persistent local CRUD operations.

## Main screens
- Pantry list
- Add/Edit Ingredient
- Suggested Recipes
- Recipe Detail
- Settings

## Important matching rule
A recipe appears only when every recipe ingredient has a matching pantry ingredient, the units are compatible, and the pantry quantity is greater than or equal to the required quantity. Partial matches are excluded.
