package es.danirod.rectball.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.model.Board;
import es.danirod.rectball.scene2d.game.BoardActor;

/**
 * Created by danirod on 13/11/15.
 */
public class LoadingBackScreen extends AbstractScreen {

    private static final float FADE_SPEED = 0.15f;
    private final Board board;

    private BoardActor boardActor;

    private Texture ballsTexture;

    private TextureAtlas ballAtlas;
    
    private boolean canUpdate;

    public LoadingBackScreen(RectballGame game) {
        super(game);
        board = new Board(6);
    }

    @Override
    void setUpInterface(Table table) {
        ballsTexture = new Texture("board/normal.png");
        TextureRegion[][] regions = TextureRegion.split(ballsTexture, 256, 256);
        ballAtlas = new TextureAtlas();
        ballAtlas.addRegion("ball_red", regions[0][0]);
        ballAtlas.addRegion("ball_yellow", regions[0][1]);
        ballAtlas.addRegion("ball_blue", regions[1][0]);
        ballAtlas.addRegion("ball_green", regions[1][1]);
        ballAtlas.addRegion("ball_gray", regions[1][2]);

        boardActor = new BoardActor(ballAtlas, board);
        Rectangle bounds = game.getState().getBoardBounds();
        boardActor.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        getStage().addActor(boardActor);
    }

    @Override
    public void show() {
        super.show();

        // Hide all balls
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                boardActor.getBall(i, j).setVisible(false);

        canUpdate = false;
        getStage().addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, FADE_SPEED),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        canUpdate = true;
                    }
                })
        ));
    }
    
    @Override
    public void render(float delta) {
        super.render(delta);
        if (canUpdate && game.manager.update(1000 / 120)) {
            canUpdate = false;
            boardActor.remove();
            ballAtlas.dispose();
            ballsTexture.dispose();
            game.finishLoading();
        } else {
            float progress = game.manager.getProgress();
            float ballPercentage = 1.0f / (6 * 6);
            int ball = 0;
            for (int j = 0; j < 6; j++) {
                for (int i = 0; i < 6; i++) {
                    if (ball * ballPercentage < progress) {
                        boardActor.getBall(i, j).setVisible(true);
                    }
                    ball++;
                }
            }
        }
    }

    @Override
    public int getID() {
        return Screens.LOADING_BACK;
    }

}
