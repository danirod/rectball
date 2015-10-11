package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import es.danirod.rectball.model.Ball;

public class BallActor extends Image {

    public Ball getBall() {
        return ball;
    }

    private class BallSelectionListener extends InputListener {

        private BoardActor board;

        private BallActor ball;

        public BallSelectionListener(BoardActor board, BallActor ball) {
            this.board = board;
            this.ball = ball;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (!isSelected()) {
                board.onBallSelected(ball);
            } else {

                board.onBallUnselected(ball);
            }
            return true;
        }
    }

    private boolean selected;

    private Ball ball;

    private BoardActor board;

    private Skin skin;

    public BallActor(BoardActor board, Ball ball, Skin skin) {
        this.board = board;
        this.ball = ball;
        this.skin = skin;
        setScaling(Scaling.fit);
        addListener(new BallSelectionListener(board, this));
    }

    @Override
    public void act(float delta) {
        if (board.isColoured()) {
            setDrawable(skin, "ball_" + ball.getColor().toString().toLowerCase());
        } else {
            setDrawable(skin, "ball_gray");
        }
        super.act(delta);
    }

    @Override
    protected void sizeChanged() {
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        setSelected(selected, true);
    }

    public void setSelected(boolean selected, boolean animate) {
        float finalScale = selected ? 0.8f : 1;
        Color finalColor = selected ? Color.LIGHT_GRAY : Color.WHITE;

        if (animate) {
            addAction(Actions.scaleTo(finalScale, finalScale, 0.15f));
            addAction(Actions.color(finalColor, 0.15f));
        } else {
            addAction(Actions.scaleTo(finalScale, finalScale));
            addAction(Actions.color(finalColor));
        }

        this.selected = selected;
    }
}
