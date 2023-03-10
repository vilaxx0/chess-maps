# ChessMaps - Chess Board Finder

A native Android mobile app developed in Kotlin and Android SDK that helps users locate public chess boards. The app uses the Google Maps API to display the locations of the chess boards. It also includes a chess clock that utilizes a WebView to render a website with chess clock functionality. Firebase was used as the backend to manage authentication and location data.

## Screenshots

<div style="display:flex; flex-direction: row;">
  <img src="./app/libs/screenshots/1.jpg" width="200" style="margin-right: 10px;" />
  <img src="./app/libs/screenshots/2.jpg" width="200" style="margin-right: 10px;" />
  <img src="./app/libs/screenshots/3.jpg" width="200" style="margin-right: 10px;" />
  <img src="./app/libs/screenshots/4.jpg" width="200" style="margin-right: 10px;" />
</div>

## Installation

To install and run the app, follow these steps:

1. Clone the repository to your local machine.

```bash
git clone https://github.com/vilaxx0/chess-maps.git
```

2. Open the project in Android Studio.
3. Create a new virtual device or connect your Android device to your computer. You can use an emulator or a physical device for testing.

   - If you're using an emulator, you can create one in Android Studio by going to "AVD Manager" and selecting "Create Virtual Device".
   - If you're using a physical device, make sure that USB debugging is enabled on your device.

4. Build and run the app in Android Studio by clicking the "Run" button.
5. The app will be installed and launched on your device automatically.

## Features

- Locate public chess boards
- Add and remove chess board locations (for authorized users only)
- View locations on Google Maps
- Chess clock functionality

## Technologies

The following technologies were used in the development of this project:

- Kotlin
- Android SDK
- Google Maps API
- Firebase
- LiveData

LiveData was used to ensure that the app remains responsive and up-to-date with the latest data of the chess boards locations. Whenever a new chess board location is added or removed, LiveData triggers the UI update, and the list of chess board locations is updated automatically.

## Contributors

This project was developed by Vilius Bučinskas If you would like to contribute to the project, please open a pull request on the GitHub repository.
