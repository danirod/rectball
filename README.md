<p align="center">
    <a href="https://github.com/danirod/rectball/releases/latest"><img src="https://img.shields.io/github/tag/danirod/rectball.svg"></a>
    <a href="http://www.gnu.org/licenses/gpl-3.0.html"><img src="https://img.shields.io/badge/license-GPL3-blue.svg"></a>
</p>

<p align="center">
  <img src="http://i.imgur.com/HLGaW33.jpg" alt="Rectball">
</p>

<p align="center">
  <a href='https://play.google.com/store/apps/details?id=es.danirod.rectball.android&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' style='width: 25%; height: 25%;'/></a>
</p>

Rectball is a colorful puzzle game for Android. Make selections, clear rectangles and make points before the time runs out. Rectball is open source and runs on Android 4.0.3+.

You can join the beta on your mobile phone or visiting [this link in your computer](https://play.google.com/apps/testing/es.danirod.rectball.android).

## Compiling the game

### Flavors

This project uses [Android SDK flavors](https://developer.android.com/studio/build/build-variants#product-flavors) to have different versions of the application:

* The OSP version is the one that can be built from sources and sideloaded onto phones right away. This is the standard open source version and if you are looking for a version free of Google privative integrations, pick this one. It only depends on the libGDX framework and the Kotlin runtime.

* The GPE version is linked against non-free SDKs such as Firebase and the Google Play Games SDK. You probably won't be able to build these versions without the proper google-services.json file too. This is the flavor I intended to use to build versions sent to the Google Play Store.

### Signing key

To automatically sign the built APK automatically when compiling using Gradle, define a file called `keystore.properties` in the project root directory with the following filled properties:

    storeFile=# the keystore file
    storePassword=# the keystore password
    keyAlias=# the key alias to use
    keyPassword=# the key password

## License

This game is released under the terms of the GNU General Public License v3.

> This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

See the COPYING file for the full text of the license.
