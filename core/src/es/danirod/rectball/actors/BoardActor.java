package es.danirod.rectball.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import es.danirod.rectball.model.Ball;
import es.danirod.rectball.model.Board;
import es.danirod.rectball.screens.GameScreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoardActor extends Table {

    private GameScreen screen;

    private Skin skin;

    private final BallActor[][] actors;

    /** The board. */
    private Board board;

    /** Is the board coloured? If false, all the balls will be grayed. */
    private boolean coloured = false;

    public BoardActor(GameScreen screen, Skin skin, Board board) {
        this.screen = screen;
        this.skin = skin;
        this.board = board;
        this.actors = new BallActor[board.getSize()][board.getSize()];

        for (int y = board.getSize() - 1; y >= 0; y--) {
            for (int x = 0; x < board.getSize(); x++) {
                actors[x][y] = new BallActor(this, board.getBall(x, y), skin);
                add(actors[x][y]).pad(2).uniform();
            }
            row();
        }
    }

    public BallActor getBall(int x, int y) {
        return actors[x][y];
    }

    public boolean isColoured() {
        return coloured;
    }

    public void setColoured(boolean coloured) {
        this.coloured = coloured;
    }

    /** The list of balls that has been selected. */
    private Set<BallActor> selectedBalls = new HashSet<>();

    protected void onBallSelected(BallActor ball) {
        selectedBalls.add(ball);
        if (selectedBalls.size() == 4) {
            selectionDone();
        } else {
            ball.setSelected(true);
        }
    }

    public void unselectBalls() {
        for (BallActor ball : selectedBalls) {
            ball.setSelected(false);
        }
        selectedBalls.clear();
    }

    /**
     * This event is invoked when the four balls have been selected.
     */
    private void selectionDone() {
        // Build the selection list from the actors.
        List<Ball> selection = new ArrayList<>();
        for (BallActor ballActor : selectedBalls) {
            selection.add(ballActor.getBall());
        }

        // Check if this is a good selection.
        if (board.selection(selection)) {
            for (BallActor ballActor : selectedBalls)
                ballActor.setSelected(false, false);
            screen.onScore(new ArrayList<>(selectedBalls));
            selectedBalls.clear();
        } else {
            unselectBalls();
        }
    }

    protected void onBallUnselected(BallActor ball) {
        selectedBalls.remove(ball);
        ball.setSelected(false);
    }
}
