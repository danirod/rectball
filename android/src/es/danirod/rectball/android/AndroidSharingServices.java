/*
 * This file is part of Rectball
 * Copyright (C) 2015 Dani Rodríguez
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

import android.content.Intent;
import android.net.Uri;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import es.danirod.rectball.RectballGame;

/**
 * This contains Android-related sharing utilities. For instance, sharing
 * screenshots from the game that can be sent to Twitter, WhatsApp or other
 * services.
 *
 * @author danirod
 * @since 0.4.0
 */
public class AndroidSharingServices implements es.danirod.rectball.platform.SharingServices {

    /** Android application instance. Required for creating intents. */
    private AndroidApplication app;

    protected AndroidSharingServices(AndroidApplication app) {
        this.app = app;
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

    /**
     * Share at the same time photo and text.
     *
     * @param pixmap  image to share
     * @param text  text to share
     */
    private void shareScreenshotWithMessage(Pixmap pixmap, String text) {
        // Let's make a dirty cast to get the game instance.
        RectballGame game = (RectballGame) app.getApplicationListener();

        try {
            // Save the pixmap to a file.
            String location = "rectball/screenshot" + System.currentTimeMillis() + ".png";
            FileHandle handle = Gdx.files.external(location);
            PixmapIO.writePNG(handle, pixmap);

            // Now attempt to share the screenshot.
            Intent sharingIntent = new Intent();
            sharingIntent.setAction(Intent.ACTION_SEND);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(handle.file()));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
            sharingIntent.setType("text/plain");
            String title = game.getLocale().get("sharing.intent");
            app.startActivity(Intent.createChooser(sharingIntent, title));
        } catch (Exception ex) {
            Gdx.app.error("SharingServices", "Couldn't share photo", ex);
        }
    }
}
