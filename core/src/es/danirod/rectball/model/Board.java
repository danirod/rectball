package es.danirod.rectball.model;

import com.badlogic.gdx.math.MathUtils;

import java.util.List;

public class Board {

    private final int size;

    private final Ball[][] balls;

    public Board(int size) {
        this.size = size;
        balls = new Ball[size][size];
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                balls[x][y] = new Ball(x, y);
    }

    public int getSize() {
        return size;
    }

    public void randomize(Coordinate bottomLeft, Coordinate upperRight) {
        BallColor[] colors = BallColor.values();
        for (int x = bottomLeft.x; x <= upperRight.x; x++) {
            for (int y = bottomLeft.y; y <= upperRight.y; y++) {
                int index = MathUtils.random(colors.length - 1);
                balls[x][y].setColor(colors[index]);
            }
        }
    }

    public void randomize() {
        randomize(new Coordinate(0, 0), new Coordinate(size - 1, size - 1));
    }

    public Ball getBall(int x, int y) {
        return balls[x][y];
    }

    /**
     * This method should be invoked when four balls have been selected. The
     * method will evaluate whether the selected balls are valid. If are valid
     * then that region will be regenerated. The result will also be returned
     * so that the client code can use the value.
     *
     * @param balls  the balls that have been selected.
     * @return  whether those balls are valid or not
     */
    public boolean selection(List<Ball> balls) {
        // If there aren't 4 balls, this is an error.
        if (balls.size() != 4)
            return false;

        // Check that the selection is valid. To be valid, four balls of the
        // same colour that form a square have to be selected. Otherwise,
        // it's not valid.
        Selection selection = new Selection(balls);
        return selection.checkSameColor() && selection.checkSquare();
    }
}
