package es.danirod.rectball.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import es.danirod.rectball.actors.Ball;
import es.danirod.rectball.model.BallColor;

/**
 * Created by danirod on 6/9/15.
 */
public class DebugBallListener extends InputListener {

    private Ball ball;

    private int pos = 0;

    private static BallColor[] COLORS = {
            BallColor.BLUE,
            BallColor.RED,
            BallColor.GREEN,
            BallColor.YELLOW
    };

    public DebugBallListener(Ball ball) {
        this.ball = ball;
        ball.setBallColor(COLORS[pos]);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        pos = (pos + 1) % COLORS.length;
        ball.setBallColor(COLORS[pos]);
        return true;
    }
}
