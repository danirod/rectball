/*
 * This file is part of Rectball.
 * Copyright (C) 2015 Dani Rodríguez
 * All rights reserved.
 */
package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import es.danirod.rectball.screens.GameScreen;

/*
 * TODO: Separate the timer logic into a separate class or interface.
 * For instance, make a Timer implements Countdown, then some timers,
 * BarTimer, CircleTimer or DigitalTimer extends Timer.
 */

/**
 * Timer is an actor that acts as a countdown during a game. The main usage
 * for this actor is to set up some time at the beginning of the game and end
 * the game when the time is over.
 *
 * @author danirod
 */
public class Timer extends Actor {

    /**
     * Warning region. When the remaining time percentage is not greater than
     * this value, the warning mode is triggered. If the time is running out
     * the actor appearance might change, for instance the progress bar could
     * be rendered using a different color or it might shake.
     */
    private static final float WARNING_TRIGGER = 0.2f;

    /**
     * The texture region used to fill the back of the timer. This background
     * can be seen as a fill on the area that represents the consumed time for
     * this timer.
     */
    private TextureRegion background;

    /**
     * The texture region used to fill the remaining time of the timer. This
     * background can be seen on the area that represents the time that the
     * user still has and usually this is the area that makes smaller and smaller
     * every frame.
     */
    private TextureRegion remaining;

    /**
     * The texture region used to fill the remaining time of the timer when the
     * time is running out. This background should be different from the normal
     * remaining background.
     */
    private TextureRegion warning;

    /**
     * The game screen owning this timer. This screen will be notified when the
     * time is over so that it can handle what happens after the time is over,
     * for instance, displaying a game over.
     */
    private GameScreen screen;
    // TODO: Change GameScreen into a lightweight interface (TimerCallback).

    /**
     * The maximum number of seconds this timer can have. When filled with
     * seconds, the value of the timer will always be clamped so that it's
     * never bigger than this. Aditionally this is used as the maximum value
     * when calculating the percentage of the timer.
     */
    private float maxSeconds;

    /**
     * The countdown value for the timer. The value of this countdown
     * represents how many seconds are there remaining until game over. The
     * value of this countdown will decrement every second and sometimes
     * could increment.
     */
    private float seconds;

    /**
     * Whether the time is running or not. This variable could be set to false
     * to make the countdown stop, for instance to make a pause and resume
     * system.
     */
    private boolean running = true;

    /**
     * Set up a new timer.
     *
     * @param screen  the screen owning this timer.
     * @param seconds  the maximum seconds for this timer.
     */
    public Timer(GameScreen screen, int seconds, Texture texture) {
        this.screen = screen;
        this.seconds = seconds;
        this.maxSeconds = seconds;

        int width = texture.getWidth() / 3;
        int height = texture.getHeight();
        background = new TextureRegion(texture, 0, 0, width, height);
        remaining = new TextureRegion(texture, height, 0, width, height);
        warning = new TextureRegion(texture, 2 * height, 0, width, height);
    }

    /**
     * How many seconds are there in the timer remaining.
     * @return  remaining value of the timer.
     */
    public float getSeconds() {
        return seconds;
    }

    /**
     * Set the number of seconds of this timer to some value. The value will
     * be clamped againts the maximum number of seconds that the timer can
     * hold.
     *
     * @param seconds new value of seconds for this timer.
     */
    public void setSeconds(float seconds) {
        this.seconds = seconds;
        if (seconds > maxSeconds) {
            this.seconds = maxSeconds;
        }
    }

    /**
     * Whether this timer is running or not.
     * @return  is the timer running or not
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Change whether the timer should be running or not. The timer won't
     * countdown unless is running. The timer can be paused by making it
     * not running.
     *
     * @param running  whether the timer should be running or not
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void act(float delta) {
        if (running) {
            seconds -= delta;
            if (seconds < 0) {
                screen.gameOver();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Draw the background of the timer.
        batch.draw(background, getX(), getY(), getWidth(), getHeight());

        // Calculate the remaining percentage of time.
        float percentage = seconds / maxSeconds;
        float remainingSize = getWidth() * percentage;

        // Render the remaining time.
        if (percentage < WARNING_TRIGGER) {
            batch.draw(warning, getX(), getY(), remainingSize, getHeight());
        } else {
            batch.draw(remaining, getX(), getY(), remainingSize, getHeight());
        }
    }
}
