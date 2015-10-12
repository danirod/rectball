package es.danirod.rectball.model;

public class Ball {

    private final int x, y;

    private BallColor color = null;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setColor(BallColor color) {
        this.color = color;
    }

    public BallColor getColor() {
        return color;
    }
}
