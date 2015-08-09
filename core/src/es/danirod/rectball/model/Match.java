package es.danirod.rectball.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Match extends Group {

    private final int size;

    private Ball[][] board;

    private Score score;

    public Match(int size) {
        this.size = size;
        this.score = new Score(123);
        this.board = new Ball[size][size];

        addActor(score);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Ball ball = new Ball(BallColor.BLUE, this, x, y);
                board[x][y] = ball;
                addActor(board[x][y]);
                board[x][y].setBounds(50 * x, 50 * y, 50, 50);
            }
        }
    }

    private int howManyBalls = 0;

    public void ballSelected(Ball me) {
        if (!me.isSelected()) {
            howManyBalls--;
            return;
        }

        if (++howManyBalls == 4) {
            List<Ball> selected = getSelectedBalls();

            Set<Integer> rows = new HashSet<>();
            Set<Integer> cols = new HashSet<>();
            for (Ball ball : selected) {
                rows.add(ball.getRow());
                cols.add(ball.getCol());
            }
            int minX, minY, maxX, maxY;

            if (!everyBallIsTheSameColor(selected)) {
                System.out.println("Hay una bola de otro color");
            } else if (rows.size() != 2 || cols.size() != 2) {
                System.out.println("No son válidas porque no es un cuadrado");
            } else {
                System.out.println("MUY BIEN :)");
            }

            for (Ball ball : selected) {
                ball.setSelected(false);
            }

            howManyBalls = 0;
        }
    }

    private boolean everyBallIsTheSameColor(List<Ball> ball) {
        BallColor color = ball.get(0).getBallColor();
        for (Ball b : ball) {
            if (b.getBallColor() != color) {
                return false;
            }
        }
        return true;
    }

    private List<Ball> getSelectedBalls() {
        List<Ball> selected = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (board[x][y].isSelected())
                    selected.add(board[x][y]);
            }
        }
        return selected;
    }

    public Ball[][] getBoard() {
        return board;
    }

    public Score getScore() {
        return score;
    }

    public int getSize() {
        return this.size;
    }

    public void reload() {
        BallColor[] allColors = BallColor.values();
        int lastIndex = -1;
        int count = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                int color = MathUtils.random(allColors.length - 1);
                if (color == lastIndex) {
                    count++;
                    if (count == 3) {
                        int nuevoColor;
                        do {
                            nuevoColor = MathUtils.random(allColors.length - 1);
                        } while (nuevoColor == lastIndex);
                    }
                } else {
                    lastIndex = color;
                }
                BallColor ballColor = allColors[MathUtils.random(allColors.length - 1)];
                board[x][y].setBallColor(ballColor);
            }
        }
    }
}
