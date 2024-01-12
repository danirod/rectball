#!/bin/sh

set -e

cd "$(dirname $0)/.."

# Build app
./gradlew lwjgl3:jpackageImage

# Prepare template
rm -rf lwjgl3/build/rectball.AppDir
cp -r lwjgl3/appimage lwjgl3/build/rectball.AppDir
cp -r lwjgl3/build/jpackage/Rectball lwjgl3/build/rectball.AppDir/usr

# Grab AppImageTool if required
APPIMAGETOOL=appimagetool-$(uname -m).AppImage
if ! [ -f lwjgl3/build/$APPIMAGETOOL ]; then
    curl -Lo lwjgl3/build/$APPIMAGETOOL https://github.com/AppImage/AppImageKit/releases/download/continuous/$APPIMAGETOOL
    chmod +x lwjgl3/build/$APPIMAGETOOL
fi

lwjgl3/build/$APPIMAGETOOL lwjgl3/build/rectball.AppDir lwjgl3/build/rectball-$(uname -m).AppImage
