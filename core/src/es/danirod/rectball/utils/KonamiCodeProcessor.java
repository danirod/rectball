package es.danirod.rectball.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class KonamiCodeProcessor extends InputAdapter {

    private static final int[] KEYS = {
            Input.Keys.UP,
            Input.Keys.UP,
            Input.Keys.DOWN,
            Input.Keys.DOWN,
            Input.Keys.LEFT,
            Input.Keys.RIGHT,
            Input.Keys.LEFT,
            Input.Keys.RIGHT,
            Input.Keys.B,
            Input.Keys.A
    };

    private int nextKey = 0;

    private KonamiCodeListener subscriber = null;

    public KonamiCodeProcessor(KonamiCodeListener subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == KEYS[nextKey]) {
            if (++nextKey == KEYS.length) {
                if (subscriber != null) {
                    subscriber.onKonamiCodePressed();
                    nextKey = 0;
                } else {
                    Gdx.app.log("KCP", "Konami Code pressed, nobody cares.");
                }
            }
        } else {
            nextKey = 0;
        }
        return true;
    }
}
