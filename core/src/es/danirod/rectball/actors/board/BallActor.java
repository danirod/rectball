package es.danirod.rectball.actors.board;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import es.danirod.rectball.model.Ball;

public class BallActor extends Image {

    public Ball getBall() {
        return ball;
    }

    private boolean selected;

    private Ball ball;

    private BoardActor board;

    private TextureAtlas atlas;

    public BallActor(BoardActor board, Ball ball, TextureAtlas atlas) {
        this.board = board;
        this.ball = ball;
        this.atlas = atlas;
        setScaling(Scaling.fit);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setSelected(!isSelected());
                return true;
            }
        });
    }

    @Override
    public void act(float delta) {
        if (board.isColoured()) {
            setDrawable(new TextureRegionDrawable(atlas.findRegion("ball_" + ball.getColor().toString().toLowerCase())));
        } else {
            setDrawable(new TextureRegionDrawable(atlas.findRegion("ball_gray")));
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
        this.selected = selected;
        if (selected) {
            board.select(ball.getX(), ball.getY());
        } else {
            board.unselect(ball.getX(), ball.getY());
        }
    }

    protected void quietlySetSelected(boolean selected) {
        this.selected = selected;
    }
}
