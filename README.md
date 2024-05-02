# Yumemi Co., Ltd. Android Engineer Code Check Assignment

## Summary

The app utilizes Kotlin, follows a single activity architecture with MVVM using data binding, and incorporates a Room database for handling favourites. It provides functionality to search GitHub repositories by name, view details, and manage favourites. Users can change the app language to English or Japanese

## Features

1) ### Welcome Page:
- Welcome Fragment that serves as the landing page on the first app launch
- Allowing users to switch between English and Japanese

2) ### GitHub Repository Search:
- Search GitHub repositories by name using the free GitHub API.
- Display search results in a RecyclerView on the home page.
- Clicking on a repository opens a details page.
- See more Git hub profile in a web view

3) ### Web Profile View:
- Explore additional GitHub profile details using the web link

4) ### Favorites:

- Save repositories as favourites in the Room database.
- View a list of saved favourites in the Favorites fragment.
- Expand items to view details and delete them.

5) ### Settings:
- Change the language from the Settings page.

6) ### UI Enhancements:

- Splash image, app launcher icon, and initial images for improved aesthetics.
- Custom action bar and bottom menu for better navigation.

7) ### Localization:

- The app supports both Japanese and English languages.

8) ### Code Quality:

- Follows MVVM architecture, data binding, and Room database best practices.
- Dependency injection using Dagger Hilt.
## Main Uis

* Light Theme

<img src="https://yumemicodetests3.s3.amazonaws.com/light_1.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/light_2.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/light_3.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/light_4.jpg" width="200"> 
<img src="https://yumemicodetests3.s3.amazonaws.com/light_5.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/light_6.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/light_7.jpg" width="200">

* Dark Theme

<img src="https://yumemicodetests3.s3.amazonaws.com/dark_1.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/dark_2.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/dark_3.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/dark_4.jpg" width="200"> 
<img src="https://yumemicodetests3.s3.amazonaws.com/dark_5.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/dark_6.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/dark_7.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/dark_8.jpg" width="200"> 
<img src="https://yumemicodetests3.s3.amazonaws.com/dark_9.jpg" width="200"> <img src="https://yumemicodetests3.s3.amazonaws.com/dark_10.jpg" width="300"> <img src="https://yumemicodetests3.s3.amazonaws.com/dark_11.jpg" width="300"> <img src="https://yumemicodetests3.s3.amazonaws.com/dark_12.jpg" width="300"> 
<img src="https://yumemicodetests3.s3.amazonaws.com/dark_13.jpg" width="300">

## Demo Videos
1) Initial App Launch and Welcome page and Main Pages
<img src="https://yumemicodetests3.s3.amazonaws.com/demo_initial_launch.gif" width="320">

2) Favourite Add, Remove feature
<img src="https://yumemicodetests3.s3.amazonaws.com/demo_add_remove.gif" width="320">

3) Portrait view with Dark theme
<img src="https://yumemicodetests3.s3.amazonaws.com/dark_theme_potrait.gif" width="320">

4) Landscape view with Dark theme
<img src="https://yumemicodetests3.s3.amazonaws.com/dark_theme_landscape.gif" width="500">


## Installation
1. Clone the repository: `git clone https://github.com/charithvithanage/android-engineer-codecheck.git`
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

## Usage
1. Open the app on your device
2. For the first time select preferred language
3. Click on the Next button to navigate to the main activity
4. Use the search functionality to find GitHub repositories.
5. Click on a repository to view its details.
6. Navigate to the repo details page and click on the "See More" button.
7. Save repositories as favourites from the details page.
8. Manage favorites from the Favorites fragment.
9. Change the app language from the Settings page.

## Project Structure

The project is organized into the following packages:

### [jp.co.yumemi.android.code_check.apiservices](https://github.com/charithvithanage/android-engineer-codecheck/tree/favourites_account_feature_branch/app/src/main/kotlin/jp/co/yumemi/android/code_check/apiservices)
- Contains classes responsible for handling API services and network requests.

### [jp.co.yumemi.android.code_check.constants](https://github.com/charithvithanage/android-engineer-codecheck/tree/main/app/src/main/kotlin/jp/co/yumemi/android/code_check/constants)
- Defines constant values used throughout the project.

### [jp.co.yumemi.android.code_check.db](https://github.com/charithvithanage/android-engineer-codecheck/tree/main/app/src/main/kotlin/jp/co/yumemi/android/code_check/db)
- Manages the database-related components, including Room database setup and entities.

### [jp.co.yumemi.android.code_check.di](https://github.com/charithvithanage/android-engineer-codecheck/tree/main/app/src/main/kotlin/jp/co/yumemi/android/code_check/di)
- Contains classes related to dependency injection using Dagger or any other dependency injection framework.

### [jp.co.yumemi.android.code_check.interfaces](https://github.com/charithvithanage/android-engineer-codecheck/tree/main/app/src/main/kotlin/jp/co/yumemi/android/code_check/interfaces)
- Defines interfaces used in the project.

### [jp.co.yumemi.android.code_check.models](https://github.com/charithvithanage/android-engineer-codecheck/tree/main/app/src/main/kotlin/jp/co/yumemi/android/code_check/models)
- Contains data models classes used to represent entities in the application.

### [jp.co.yumemi.android.code_check.repository](https://github.com/charithvithanage/android-engineer-codecheck/tree/main/app/src/main/kotlin/jp/co/yumemi/android/code_check/repositories)
- Manages the data repository, handling the flow of data between the database, network, and UI.

### [jp.co.yumemi.android.code_check.ui.activities](https://github.com/charithvithanage/android-engineer-codecheck/tree/main/app/src/main/kotlin/jp/co/yumemi/android/code_check/ui)
- Contains UI classes ( Activity, Fragment, Dialog )

### [jp.co.yumemi.android.code_check.ui.bindadapters](https://github.com/charithvithanage/android-engineer-codecheck/tree/main/app/src/main/kotlin/jp/co/yumemi/android/code_check/ui/bindadapters)
- Includes custom data binding adapters.

### [jp.co.yumemi.android.code_check.utils](https://github.com/charithvithanage/android-engineer-codecheck/tree/main/app/src/main/kotlin/jp/co/yumemi/android/code_check/utils)
- Includes utility classes and helper functions used across the application.


## Main Libraries and Dependencies
- Room Database: Version 2.6.0
- Retrofit: Version 2.9.0
- Dagger Hilt: Version 2.48.1
- Coil: Version 2.5.0
- Kotlin Coroutines: Version 1.7.3
- Navigation Component: Version 2.7.5
- Espresso: Version 3.5.1

## Environment

- IDE：Android Studio Giraffe | 2022.3.1 Patch 2
- Kotlin：1.6.21
- Java：17
- Gradle：8.1.3
- minSdk：23
- targetSdk：34

