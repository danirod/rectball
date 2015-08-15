package es.danirod.rectball.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import es.danirod.rectball.AssetLoader;

public class Timer extends Actor {

    private TextureRegion background, activeBackground;

    /** The match where everything happens. */
    private Match match;

    /** Maximum number of seconds the user can play. */
    private float maxSeconds;

    /** The current seconds remaining for game over. */
    private float seconds;

    /** Whether the time goes on or not. */
    private boolean running = true;

    public Timer(Match match, int seconds) {
        this.match = match;
        maxSeconds = this.seconds = seconds;

        Texture tex = AssetLoader.get().get("timer.png", Texture.class);
        background = new TextureRegion(tex, 0, 0, 10, 20);
        activeBackground = new TextureRegion(tex, 10, 0, 10, 20);
    }

    public float getSeconds() {
        return seconds;
    }

    public void setSeconds(float seconds) {
        this.seconds = seconds;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void act(float delta) {
        if (running) {
            seconds -= delta;
            if (seconds < 0) {
                match.gameOver();
            }
        }
    }

    public void increment(float amount) {
        seconds += amount;
        if (seconds > maxSeconds)
            seconds = maxSeconds;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(background, getX(), getY(), getWidth(), getHeight());

        float timePercentage = seconds / maxSeconds;
        float activeBarWidth = getWidth() * timePercentage;
        batch.draw(activeBackground, getX(), getY(), activeBarWidth, getHeight());
    }
}
