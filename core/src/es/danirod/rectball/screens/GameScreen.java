/*
 * This file is part of Rectball.
 * Copyright (C) 2015 Dani Rodríguez.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Ball;
import es.danirod.rectball.actors.Board;
import es.danirod.rectball.actors.Timer;
import es.danirod.rectball.actors.Timer.TimerCallback;
import es.danirod.rectball.dialogs.PauseDialog;
import es.danirod.rectball.listeners.BallInputListener;
import es.danirod.rectball.model.BallColor;
import es.danirod.rectball.model.Bounds;
import es.danirod.rectball.model.CombinationFinder;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;
import es.danirod.rectball.utils.StyleFactory;

public class GameScreen extends AbstractScreen implements TimerCallback {

    public Board board;

    public Label score;

    public Timer timer;

    private int valueScore;

    private boolean paused;

    private PauseDialog pauseDialog;

    public GameScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void load() {
        // Set up the board.
        String file = game.settings.isColorblind() ? "colorblind" : "normal";
        Texture sheet = game.manager.get("board/" + file + ".png");
        board = new Board(this, sheet, 7, game.player);

        // Set up the listeners for the board.
        Ball[][] allBalls = board.getBoard();
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                Ball ball = allBalls[x][y];
                ball.addListener(new BallInputListener(ball, board));
            }
        }

        // Set up the score
        Texture numbers = game.manager.get("scores.png");
        score = buildScoreLabel();

        timer = new Timer(30, game.getSkin());
        timer.addSubscriber(this);

        super.load();
    }

    @Override
    public int getID() {
        return Screens.GAME;
    }

    @Override
    public void show() {
        super.show();

        // Capture Back button so that the game doesn't minimize on Android.
        Gdx.input.setCatchBackKey(true);

        // Reset data
        valueScore = 0;
        game.aliveTime = 0;
        paused = false;

        // Reset data
        board.setTouchable(Touchable.disabled);
        score.setFontScale(6f);
        score.setText(buildScore(valueScore, 4));
        timer.setRunning(false);

        // Set up the pause dialog.
        Texture dialogTexture = new Texture("ui/leave.png");
        TextureRegion dialogRegion = new TextureRegion(dialogTexture);
        TextureRegionDrawable dialogDrawable = new TextureRegionDrawable(dialogRegion);

        Texture buttonTexture = new Texture("ui/button.png");
        TextureRegion normalButton = new TextureRegion(buttonTexture, 0, 0, 128, 128);
        TextureRegion selectedButton = new TextureRegion(buttonTexture, 128, 0, 128, 128);
        BitmapFont titleFont = game.manager.get("bigFont.ttf");
        BitmapFont regularFont = game.manager.get("normalFont.ttf");
        WindowStyle pauseStyle = new WindowStyle(titleFont, Color.WHITE, dialogDrawable);

        // Create buttons.
        TextButtonStyle leaveButtonStyle = StyleFactory.buildTextButtonStyle(normalButton,
                selectedButton, 32, regularFont);
        LabelStyle titleStyle = new LabelStyle(titleFont, Color.WHITE);

        pauseDialog = new PauseDialog(pauseStyle, titleStyle, leaveButtonStyle);
        pauseDialog.addYesButtonCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // make dialog invisible
                PauseDialog dialog = (PauseDialog)actor.getParent();
                dialog.setVisible(false);

                timer.setRunning(false);
                onTimeOut();
            }
        });
        pauseDialog.addNoButtonCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // force uncheck
                TextButton button = (TextButton)actor;
                button.setChecked(false);

                setPaused(false);
            }
        });

        pauseDialog.setVisible(false);

        Ball[][] allBalls = board.getBoard();
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                allBalls[x][y].addAction(Actions.sequence(
                        Actions.scaleTo(0, 0),
                        Actions.delay(MathUtils.random(0.1f, 2.5f)),
                        Actions.scaleTo(1, 1, 0.5f)
                ));
            }
        }

        board.addAction(Actions.sequence(
                Actions.delay(3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        board.randomize();
                        board.setTouchable(Touchable.enabled);
                        timer.setRunning(true);
                    }
                })
        ));
    }

    @Override
    public void setUpInterface(Table table) {
        table.setDebug(true);
        table.add(score).fillX().height(100).row();
        table.add(timer).fillX().height(60).padTop(20).padBottom(20).row();
        table.add(board).expand().fill().row();
    }

    @Override
    public void hide() {
        // Restore original back button functionality.
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (timer.isRunning()) {
            game.aliveTime += delta;
        }

        // Pause the game when you press BACK or ESCAPE.
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            setPaused(!paused);
        }
    }

    @Override
    public void onTimeOut() {
        // Update the score... and the record.
        long lastScore = Long.parseLong(score.getText().toString());
        game.scores.addScore(lastScore);

        timer.setRunning(false);
        board.setTouchable(Touchable.disabled);

        game.player.playSound(SoundCode.GAME_OVER);

        // Get a combination that the user didn't find.
        CombinationFinder finder = new CombinationFinder(board.getBoard());
        Bounds combination = finder.getCombination();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        Ball[][] allBalls = board.getBoard();
        BallColor refColor = allBalls[combination.minX][combination.minY].getBallColor();
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                final Ball currentBall = allBalls[x][y];

                if ((x >= combination.minX && x <= combination.maxX) &&
                        (y >= combination.minY && y <= combination.maxY)) {
                    currentBall.setBallColor(refColor);
                    continue;
                }

                float desplX = MathUtils.random(-width / 2, width / 2);
                float desplY = -height - MathUtils.random(0, height / 4);
                float scaling = MathUtils.random(0.3f, 0.7f);
                float desplTime = MathUtils.random(0.5f, 1.5f);
                currentBall.addAction(Actions.sequence(
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                currentBall.setBallColor(BallColor.GRAY);
                            }
                        }),
                        Actions.parallel(
                                Actions.moveBy(desplX, desplY, desplTime),
                                Actions.scaleBy(scaling, scaling, desplTime)
                        )));
            }
        }

        getStage().addAction(Actions.sequence(
                Actions.delay(2f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        game.setScreen(Screens.GAME_OVER);
                    }
                })
        ));
    }

    private Label buildScoreLabel() {
        BitmapFont font = game.manager.get("fonts/scores.fnt");
        LabelStyle style = new LabelStyle(font, Color.WHITE);
        Label label = new Label("0", style);
        label.setAlignment(Align.center);
        return label;
    }

    private String buildScore(int value, int digits) {
        String strValue = Integer.toString(value);
        while (strValue.length() < digits) {
            strValue = "0" + strValue;
        }
        return strValue;
    }

    public void score(int score, BallColor color, int rows, int cols) {
        // Store this information in the statistics.
        String size = Math.max(rows, cols) + "x" + Math.min(rows, cols);
        game.statistics.getTotalData().incrementValue("balls", rows * cols);
        game.statistics.getTotalData().incrementValue("score", score);
        game.statistics.getTotalData().incrementValue("combinations");
        game.statistics.getColorData().incrementValue(color.toString().toLowerCase());
        game.statistics.getSizesData().incrementValue(size);

        valueScore += score;
        this.score.setText(buildScore(valueScore, 4));

        BitmapFont font = game.manager.get("fonts/scores.fnt");
        LabelStyle style = new LabelStyle(font, Color.WHITE);
        final Label scoreLabel = new Label(Integer.toString(score), style);
        scoreLabel.setAlignment(Align.center);

        float ballSize = board.getWidth() / board.getSize();
        scoreLabel.setSize(ballSize, ballSize);
        scoreLabel.setX(Gdx.graphics.getWidth() / 2 - scoreLabel.getWidth() / 2);
        scoreLabel.setY(Gdx.graphics.getHeight() / 2 - scoreLabel.getHeight() / 2);
        scoreLabel.setFontScale(5);
        scoreLabel.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, 100, 1),
                        Actions.fadeOut(1)
                ),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        scoreLabel.remove();
                    }
                })
        ));
        getStage().addActor(scoreLabel);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        timer.setRunning(!paused);
        board.setMasked(paused);
        board.setTouchable(paused ? Touchable.disabled : Touchable.enabled);
        pauseDialog.setVisible(paused);
    }

    @Override
    public void pause() {
        setPaused(true);
    }
}
