[![Build Status](https://travis-ci.org/danirod/rectball.svg)](https://travis-ci.org/danirod/rectball)
[![Coverage Status](https://coveralls.io/repos/github/danirod/rectball/badge.svg?branch=devel)](https://coveralls.io/github/danirod/rectball?branch=devel)
[![GitHub tag](https://img.shields.io/github/tag/danirod/rectball.svg)](https://github.com/danirod/rectball/releases/latest)
[![GitHub license](https://img.shields.io/badge/license-GPL3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)

![Rectball](docs/logo.jpg)

Rectball is a colorful puzzle game for Android. Make selections, clear rectangles and make points before the time runs out. Rectball is open source and runs on Android 2.3+.

This is the open source repository for the Rectball code base. If you just want to download and play Rectball on your desktop, download the latest release [for Android](https://play.google.com/store/apps/details?id=es.danirod.rectball.android) and [PC (Windows / Linux / MacOS)](http://github.com/danirod/rectball/releases/latest).

## Building

This project uses Gradle as build tool. You can use Gradle without installing anything right from your command line, as described below. It is also possible to integrate the project onto your IDE of choice. There are plugins for NetBeans and Eclipse. IntelliJ IDEA and Android Studio supports Gradle projects out of the box.

JDK 7 or greater is required to build the repository. Although JDK 8 is fully supported, source code uses Java 7 for Android compatibility.

Open a Terminal if you are on Linux or MacOS X, or PowerShell / Command Prompt if you are on Windows, and go to the folder where you previously cloned Rectball. Use the following commands to build and run the software on Desktop and on Android.

### Building the desktop version

```bash
$ ./gradlew desktop:dist       on UNIX
> gradlew.bat desktop:dist     on Windows
```

The generated JAR can be found in **desktop/build/libs**.

To run the software, use:

```bash
$ ./gradlew desktop:run        on UNIX
> gradlew.bat desktop:run      on Windows
```

### Building the Android version

```bash
$ ./gradlew android:assembleRelease     on UNIX
> gradlew.bat android:assembleRelease   on Windows
```

The generated APK can be found in **android/build/outputs/apk**. It cannot be installed in any Android phone unless it is signed. To quickly debug the software in your phone, connect your phone to your computer in USB debugging mode and run the following command:

```bash
$ ./gradlew android:installDebug android:run    on UNIX
> gradlew.bat android:installDebug android:run  on Windows
```

## License

This game is released under the terms of the GNU General Public License v3.

> This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

See the COPYING file for the full text of the license.
