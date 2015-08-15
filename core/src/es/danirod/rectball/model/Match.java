package es.danirod.rectball.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.*;
import java.util.stream.Collectors;

public class Match extends Group {

    private final int size;

    private Ball[][] board;

    private Score score;

    public Match(int size) {
        this.size = size;

        this.score = new Score();
        score.setPosition(0, Gdx.graphics.getHeight() - 80);
        score.setSize(Gdx.graphics.getWidth(), 80);

        this.board = new Ball[size][size];

        addActor(score);

        float ballSize = Gdx.graphics.getWidth() / size;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Ball ball = new Ball(BallColor.BLUE, this, x, y);
                board[x][y] = ball;
                addActor(board[x][y]);
                board[x][y].setBounds(ballSize * x, ballSize * y, ballSize, ballSize);
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

            if (!everyBallIsTheSameColor(selected)) {

            } else if (rows.size() != 2 || cols.size() != 2) {

            } else {
                int minX, maxX, minY, maxY;
                minX = minY = Integer.MAX_VALUE;
                maxX = maxY = Integer.MIN_VALUE;
                for (Ball ball : selected) {
                    minX = Math.min(minX, ball.getRow());
                    minY = Math.min(minY, ball.getCol());
                    maxX = Math.max(maxX, ball.getRow());
                    maxY = Math.max(maxY, ball.getCol());
                }

                int rowLength = maxY - minY + 1;
                int colLength = maxX - minX + 1;

                score.increment(rowLength * colLength);

                reload(minX, minY, maxX, maxY);
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
        reload(0, 0, board.length - 1, board.length - 1);
    }

    public void reload(int x1, int y1, int x2, int y2) {
        BallColor[] allColors = BallColor.values();
        int lastIndex = -1;
        int count = 0;
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
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
