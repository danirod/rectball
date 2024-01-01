/* This file is part of Rectball
 * Copyright (C) 2015-2024  Dani Rodríguez
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.scene2d.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.List;

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
public class TimerActor extends Actor {

    /**
     * Warning region. When the remaining time percentage is not greater than
     * this value, the warning mode is triggered. If the time is running out
     * the actor appearance might change, for instance the progress bar could
     * be rendered using a different color or it might shake.
     */
    private static final float WARNING_TRIGGER = 0.2f;
    private final List<TimerCallback> subscribers = new ArrayList<>();
    /**
     * The maximum number of seconds this timer can have. When filled with
     * seconds, the value of the timer will always be clamped so that it's
     * never bigger than this. Additionally this is used as the maximum value
     * when calculating the percentage of the timer.
     */
    private final float maxSeconds;
    /**
     * The skin used by the game.
     */
    private final Skin skin;
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

    private float remainingTime, remainingTimeSpeed;

    /**
     * Set up a new timer.
     *
     * @param seconds the maximum seconds for this timer.
     */
    public TimerActor(int seconds, Skin skin) {
        this.seconds = seconds;
        this.maxSeconds = seconds;
        this.skin = skin;
    }

    /**
     * Add a new subscriber to this timer. It will be notified when the time
     * is over so that it can define its own functionality. For instance, the
     * screen might display a game over message.
     *
     * @param subscriber the subscriber that is going to be added.
     */
    public void addSubscriber(TimerCallback subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * How many seconds are there in the timer remaining.
     *
     * @return remaining value of the timer.
     */
    public float getSeconds() {
        return seconds;
    }

    /**
     * Set the number of seconds of this timer to some value. The value will
     * be clamped against the maximum number of seconds that the timer can
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
     *
     * @return is the timer running or not
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Change whether the timer should be running or not. The timer won't
     * countdown unless is running. The timer can be paused by making it
     * not running.
     *
     * @param running whether the timer should be running or not
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void act(float delta) {
        if (running) {
            seconds -= delta;
        }

        if (remainingTime > 0) {
            float givenTime = Math.min(remainingTime, remainingTimeSpeed);
            remainingTime -= givenTime;
            seconds += givenTime;
        }

        // Clamp the score both on max seconds and on min seconds.
        seconds = Math.min(seconds, maxSeconds);
        if (seconds < 0) {
            seconds = 0;
            for (TimerCallback subscriber : subscribers) {
                subscriber.onTimeOut();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        // Draw the progress bar background.
        Texture progress = skin.get("progress", Texture.class);
        batch.setColor(0.5f, 0.5f, 0.5f, color.a * parentAlpha);
        batch.draw(progress, getX(), getY(), getWidth(), getHeight());

        // Calculate the remaining percentage of time.
        float percentage = seconds / maxSeconds;
        float remainingSize = getWidth() * percentage;

        // Render the remaining time using the appropriate color.
        if (percentage < WARNING_TRIGGER) {
            batch.setColor(color.r, 0, 0, color.a * parentAlpha);
        } else {
            batch.setColor(0, color.g, 0, color.a * parentAlpha);
        }
        batch.draw(progress, getX(), getY(), remainingSize, getHeight());

        batch.setColor(color.r, color.g, color.b, color.a);
    }

    public void giveTime(float time, float speed) {
        remainingTime += time;
        remainingTimeSpeed = speed;
    }

    /**
     * This interface uses the Subscriber pattern to be notified when the timer
     * runs out of time. Make your subscriber class implement this interface,
     * then pass your subscriber to this timer when you create it. It will be
     * notified that the time is over.
     */
    public interface TimerCallback {

        /**
         * This is the method that receives the message that the time is over.
         */
        void onTimeOut();

    }
}
