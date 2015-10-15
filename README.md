# Rectball

[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/danirod/rectball?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

This is the source code for Rectball.

## Building

**NOTE**: Java 7 or greater is required to build this software.

Clone the repository or download the tarball or zipball.

Open Terminal if you are on Linux or OS X or open PowerShell if you are on Microsoft Windows. Navigate to the directory where the source code that you have just downloaded is in, and build the software using Gradle.

### Building the desktop version

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

The generated APK can be found in **android/build/outputs/apk**. It cannot be installed in any Android phone unless it is signed.

To quickly debug the software in your phone, connect your phone to your computer in USB debugging mode and run the following command:

```bash
$ ./gradlew android:installDebug android:run    on UNIX
> gradlew.bat android:installDebug android:run  on Windows
```

## License

This game is released under the terms of the GNU General Public License v3.

> This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

See the COPYING file for the full text of the license.
