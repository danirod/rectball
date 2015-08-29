package es.danirod.rectball.listeners;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import es.danirod.rectball.actors.Ball;
import es.danirod.rectball.actors.Board;

/**
 * Created by danirod on 29/8/15.
 */
public class BallInputListener extends InputListener {

    private final Board board;

    private final Ball ball;

    public BallInputListener(Ball ball, Board board) {
        this.ball = ball;
        this.board = board;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        ball.setSelected(!ball.isSelected());
        if (ball.isSelected()) {
            ball.addAction(Actions.scaleTo(0.8f, 0.8f, 0.1f));
        } else {
            ball.addAction(Actions.scaleTo(1f, 1f, 0.1f));
        }
        board.ballSelected(ball);
        return true;
    }

}
