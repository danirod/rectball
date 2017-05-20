package es.danirod.rectball.android;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidPreferences;
import com.badlogic.gdx.files.FileHandle;

import es.danirod.rectball.platform.LegacyScores;
import es.danirod.rectball.platform.LegacyStatistics;
import es.danirod.rectball.platform.Platform;
import es.danirod.rectball.platform.Scores;
import es.danirod.rectball.platform.Sharing;
import es.danirod.rectball.platform.Statistics;

abstract class AndroidPlatformBase implements Platform {

    private final Sharing sharing;

    private final Scores score;

    private final Preferences preferences;

    private final Statistics statistics;

    private final AndroidApplication app;

    protected AndroidPlatformBase(AndroidLauncher app) {
        this.app = app;

        sharing = new AndroidSharing(app);
        score = new LegacyScores() {
            @Override
            protected FileHandle getScoresFile() {
                return Gdx.files.local("scores");
            }
        };
        statistics = new LegacyStatistics() {
            @Override
            protected FileHandle getStatistics() {
                return Gdx.files.local("stats");
            }
        };
        preferences = new AndroidPreferences(app.getSharedPreferences("rectball", Context.MODE_PRIVATE));
    }


    @Override
    public Sharing sharing() {
        return sharing;
    }

    @Override
    public Scores score() {
        return score;
    }

    @Override
    public Preferences preferences() {
        return preferences;
    }

    @Override
    public Statistics statistics() {
        return statistics;
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

    public void onStart() {

    }

    public void onStop() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
