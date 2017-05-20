package es.danirod.rectball.android;

import com.badlogic.gdx.Gdx;

import es.danirod.rectball.platform.GoogleServices;

public class NullGameServices implements GoogleServices {

    @Override
    public void signIn() {
        Gdx.app.log("NullGameServices", "signIn()");
    }

    @Override
    public void signOut() {
        Gdx.app.log("NullGameServices", "signOut()");
    }

    @Override
    public boolean isSignedIn() {
        Gdx.app.log("NullGameServices", "isSignedIn()");
        return false;
    }

    @Override
    public void uploadScore(int score, int time) {
        Gdx.app.log("NullGameServices", "uploadScore(score=" + score + ", time=" + time +")");
    }

    @Override
    public void showLeaderboards() {
        Gdx.app.log("NullGameServices", "showLeaderboards()");
    }

    @Override
    public void showAchievements() {
        Gdx.app.log("NullGameServices", "showAchievements()");
    }
}
