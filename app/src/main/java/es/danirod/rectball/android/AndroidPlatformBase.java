/*
 * This file is part of Rectball
 * Copyright (C) 2015-2017 Dani Rodríguez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.android;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.io.File;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.platform.Platform;

abstract class AndroidPlatformBase implements Platform {

    private final AndroidApplication app;

    AndroidPlatformBase(AndroidLauncher app) {
        this.app = app;
    }

    @Override
    public void toast(final CharSequence msg) {
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(app, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void shareScreenshot(Pixmap pixmap) {
        Gdx.app.debug("SharingServices", "Requested sharing a screenshot");
        shareScreenshotWithMessage(pixmap, "");
    }

    @Override
    public void shareGameOverScreenshot(Pixmap pixmap, int score, int time) {
        Gdx.app.debug("SharingServices", "Requested sharing a screenshot");

        // Let's make a dirty cast to get the game instance.
        RectballGame game = (RectballGame) app.getApplicationListener();
        String message = game.getLocale().format("sharing.text", score);
        message += " https://play.google.com/store/apps/details?id=es.danirod.rectball.android";
        shareScreenshotWithMessage(pixmap, message);
    }

    @Override
    public void openInStore() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + AndroidLauncher.PACKAGE));
            app.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + AndroidLauncher.PACKAGE));
            app.startActivity(intent);
        }
    }

    private void shareScreenshotWithMessage(Pixmap pixmap, String text) {
        // Let's make a dirty cast to get the game instance.
        RectballGame game = (RectballGame) app.getApplicationListener();

        try {
            Uri screenshot = createScreenshotURI(pixmap);
            Intent sharingIntent = shareScreenshotURI(screenshot, text);
            String title = game.getLocale().get("sharing.intent");
            app.startActivity(Intent.createChooser(sharingIntent, title));
        } catch (Exception ex) {
            Gdx.app.error("SharingServices", "Couldn't share photo", ex);
        }
    }

    private Uri createScreenshotURI(Pixmap pixmap) {
        /*
            FIXME: THIS HACK MAKES GOD KILL KITTENS
            Should investigate on how to use the Android compatibility library.
            However, even the compatibility library seems to break compatibility
            since I cannot share anymore using SMS. Oh, well, how beautifully
            broken Android seems to be anyway.
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Use the fucking new Android permission system.
            File sharingPath = new File(app.getFilesDir(), "rectball-screenshots");
            File newScreenshot = new File(sharingPath, "screenshot.png");
            FileHandle screenshotHandle = Gdx.files.absolute(newScreenshot.getAbsolutePath());
            PixmapIO.writePNG(screenshotHandle, pixmap);
            return FileProvider.getUriForFile(app, app.getString(R.string.provider), newScreenshot);
        } else {
            // Use the fucking old Android permission system.
            FileHandle sharingPath = Gdx.files.external("rectball");
            sharingPath.mkdirs();
            FileHandle newScreenshot = Gdx.files.external("rectball/screenshot.png");
            PixmapIO.writePNG(newScreenshot, pixmap);
            return Uri.fromFile(newScreenshot.file());
        }
    }

    private Intent shareScreenshotURI(Uri uri, CharSequence message) {
        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, message);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return sharingIntent;
    }

    public void onStart() {

    }

    public void onStop() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
