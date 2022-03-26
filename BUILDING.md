# Build Instructions

This game is written using libGDX but I do not use the regular directory structure proposed by
libGDX. Instead, because there is only a version for Android, I use the standard application
structure proposed by Android. The entire application is located in the app/ directory. This
application can be built either with Android Studio or with any other development environment
that can work with Android projects making use of Gradle.

## A brief on the flavours

This application makes use of two flavours:

* **osp**: this is the main and open source edition. This is the one that produces artifacts
that only use free or open source licences. It is also easier to build this version since it
does not require in special Google SDKs such as Google Play Games.

* **gpe**: this is the variant that additionally links the game against the Google Play Games
SDK as well as other non-free libs that are useful to build and deploy a version to the Google
Play Store. You probably don't want to waste your time here unless you specifically want to
build a variant with support for the Google SDKs.

## Files required to build GPE

To build the GPE variant it is required to provide two files that have to be downloaded from
the different Google consoles.

* The games-ids.xml file can be downloaded from the Google Play Games console inside the
Google Play Console. This file must be located in `app/src/gpe/res/values/games-ids.xml`. The
project will not compile without this file and you will usually see an error saying that
"@strings/app_id" is not known.
* The google-services.json file must be updated with the real google-services.json that can
be downloaded from the Firebase project. This file is used for Firebase Analytics, although
I think that I could eventually remove this since I don't think I am using Analytics anymore.

## Signing key

To automatically sign the built APK automatically when compiling using Gradle, define a file called
`keystore.properties` in the project root directory with the following filled properties:

    storeFile=# the keystore file
    storePassword=# the keystore password
    keyAlias=# the key alias to use
    keyPassword=# the key password
