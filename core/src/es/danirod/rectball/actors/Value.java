package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Value extends Actor {

    /**
     * How many characters have to be rendered. If the number to be rendered
     * has less digits than this value, it will be padded with zeroes.
     */
    private final int length;

    /** Size of the character in pixels. */
    private final int size;

    /** Numbers array. Each region is a number. */
    private final TextureRegion[] numbers;

    /** The digits that are rendered. */
    private Sprite[] digits;

    private long value;

    public Value(Texture sheet, int length, long value) {
        this.length = length;
        this.value = value;
        size = sheet.getHeight();
        numbers = setUpDigits(sheet);
        digits = new Sprite[length];
        updateDigits();
    }

    private TextureRegion[] setUpDigits(Texture numbers) {
        TextureRegion[] regions = new TextureRegion[10];
        for (int i = 0; i < 10; i++) {
            regions[i] = new TextureRegion(numbers, size * i, 0, size, size);
        }
        return regions;
    }

    public long getValue() {
        return value;
    }

    public long increment(long howMuch) {
        this.value += howMuch;
        updateDigits();
        return this.value;
    }

    private String scoreAsString() {
        String scoreString = Long.toString(value);
        int numeroDeZeros = length - scoreString.length();
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
            if (digits[ch] == null) {
                digits[ch] = new Sprite(numbers[index]);
            } else {
                digits[ch].setRegion(numbers[index]);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < length; i++) {
            digits[i].draw(batch, parentAlpha);
        }
    }

    /** Escala a la que tiene que renderizarse el marcador. */
    private int scale = 1;

    @Override
    protected void sizeChanged() {
        // Calculate the scale the digits have to be rendered at.
        float glyphMaxWidth = getWidth() / length;
        float glyphMaxHeight = getHeight();
        int scale = (int) Math.min(glyphMaxWidth, glyphMaxHeight) / size;

        // The height in pixels for the drawn characters.
        int spriteSize = scale * size;

        // Now let's go for horizontal and vertical centering!
        // This is the total width and height the digits are using.
        int totalWidth = spriteSize * length;
        int totalHeight = spriteSize;

        // And now we calculate the offset for these digits.
        float baseX = getX() + (getWidth() - totalWidth) / 2;
        float baseY = getY() + (getHeight() - totalHeight) / 2;

        // Finally set the bounds.
        for (int i = 0; i < length; i++) {
            digits[i].setPosition(baseX + i * spriteSize, baseY);
            digits[i].setSize(spriteSize, spriteSize);
        }
    }
}
