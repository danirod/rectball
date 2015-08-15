package es.danirod.rectball.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import es.danirod.rectball.AssetLoader;
import es.danirod.rectball.screens.GameScreen;

public class Score extends Actor {

    private static final int NUM_CHARS = 6;

    private GameScreen screen;

    private TextureRegion[] numbers;

    private TextureRegion[] digits;

    private long score;


    public Score(GameScreen screen, long score) {
        this.screen = screen;
        digits = new TextureRegion[NUM_CHARS];
        this.score = score;
        Texture numbers = AssetLoader.get().get("scores.png", Texture.class);
        this.numbers = new TextureRegion[10];
        for (int i = 0; i < 10; i++) {
            this.numbers[i] = new TextureRegion(numbers, 8 * i, 0, 8, 8);
        }
        updateDigits();
    }

    public long getScore() {
        return score;
    }

    public long increment(long howMuch) {
        this.score += howMuch;
        updateDigits();
        return this.score;
    }

    private String scoreAsString() {
        String scoreString = Long.toString(score);
        int numeroDeZeros = 6 - scoreString.length();
        String scoreFinal = "";
        for (int i = 0; i < numeroDeZeros; i++) {
            scoreFinal += "0";
        }
        scoreFinal += scoreString;
        return scoreFinal;
    }

    private void updateDigits() {
        String scoreString = scoreAsString();
        for (int ch = 0; ch < scoreString.length(); ch++) {
            int index = scoreString.charAt(ch) - '0';
            digits[ch] = numbers[index];
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int totalWidth = scale * NUM_CHARS;
        float baseX = getX();// + getWidth() / 2 - totalWidth / (2 * 6);
        for (int i = 0; i < NUM_CHARS; i++) {
            int charSize = 8 * scale;
            float y = getY();
            float x = baseX + (i * charSize);
            batch.draw(digits[i], x, y, charSize, charSize);
        }
    }

    /** Escala a la que tiene que renderizarse el marcador. */
    private int scale = 1;

    @Override
    protected void sizeChanged() {
        int width = (int) getWidth();
        int height = (int) getHeight();
        int min = Math.min(width, height);
        scale = min / 8;
    }
}
