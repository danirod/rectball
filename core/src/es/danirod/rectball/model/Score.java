package es.danirod.rectball.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import es.danirod.rectball.AssetLoader;

public class Score extends Actor {

    private static final int NUM_CHARS = 6;

    private TextureRegion[] numbers;

    private TextureRegion[] digits;

    private long score;

    public Score() {
        this(0);
    }

    public Score(long score) {
        digits = new TextureRegion[NUM_CHARS];
        this.score = score;
        Texture numbers = AssetLoader.get().get("scores.png", Texture.class);
        this.numbers = new TextureRegion[10];
        for (int i = 0; i < 10; i++) {
            this.numbers[i] = new TextureRegion(numbers, 64 * i, 0, 64, 64);
        }
        updateDigits();
    }

    public long getScore() {
        return score;
    }

    public long increment(long howMuch) {
        this.score += howMuch;
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
        int height = Gdx.graphics.getHeight();
        for (int i = 0; i < NUM_CHARS; i++) {
            batch.draw(digits[i], 64 * i, height - 64, 64, 64);
        }
    }
}
