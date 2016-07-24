package es.danirod.rectball.scene2d.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.model.Ball;

public class TableroActor extends Table {

    final RectballGame game;

    final TextureAtlas balls;

    public TableroActor(RectballGame game) {
        this.game = game;
        this.balls = game.getBallAtlas();
        int size = game.getState().getBoard().getSize();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                add(new BallActor(game.getState().getBoard().getBall(x, y))).grow();
            }
            row();
        }
    }

    class BallActor extends Image {

        final Ball ball;

        public BallActor(Ball ball) {
            this.ball = ball;
            setScaling(Scaling.fit);
            updateRegion();
        }

        @Override
        public void act(float delta) {
            updateRegion();
        }

        void updateRegion() {
            TextureRegion region;
            if (ball == null) {
                String color = "ball_" + ball.getColor().toString();
                if ((region = balls.findRegion(color)) == null) {
                    region = balls.findRegion("ball_gray");
                }
            } else {
                region = balls.findRegion("ball_gray");
            }
            setDrawable(new TextureRegionDrawable(region));
        }
    }

}
