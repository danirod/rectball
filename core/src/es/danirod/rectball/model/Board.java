package es.danirod.rectball.model;

import com.badlogic.gdx.math.MathUtils;

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
                int index = MathUtils.random(colors.length - 2);
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
}
