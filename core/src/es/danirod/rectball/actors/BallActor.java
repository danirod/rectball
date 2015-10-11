package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import es.danirod.rectball.model.Ball;

public class BallActor extends Image {

    private Ball ball;

    private BoardActor board;

    private Skin skin;

    private TextureAtlas balls;

    public BallActor(BoardActor board, Ball ball, Skin skin) {
        this.board = board;
        this.ball = ball;
        this.skin = skin;
        setScaling(Scaling.fit);
    }

    @Override
    public void act(float delta) {
        if (board.isColoured()) {
            setDrawable(skin, "ball_" + ball.getColor().toString().toLowerCase());
        } else {
            setDrawable(skin, "ball_gray");
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
