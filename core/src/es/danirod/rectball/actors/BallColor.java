package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum BallColor {

    // TODO: Test that the rows and columns aren't bad.

    BLUE(1, 0),

    RED(0, 1),

    GREEN(1, 1),

    YELLOW(0, 0);

    private int row, col;

    BallColor(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public TextureRegion getRegion(Texture source) {
        int ball = source.getWidth() / 2;
        return new TextureRegion(source, ball * col, ball * row, ball, ball);
    }

};
