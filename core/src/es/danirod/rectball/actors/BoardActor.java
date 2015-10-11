package es.danirod.rectball.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import es.danirod.rectball.model.Board;

public class BoardActor extends Table {

    private Skin skin;

    /** The board. */
    private Board board;

    /** Is the board coloured? If false, all the balls will be grayed. */
    private boolean coloured = false;

    public BoardActor(Skin skin, Board board) {
        this.skin = skin;
        this.board = board;

        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                BallActor ball = new BallActor(this, board.getBall(x, y), skin);
                add(ball).pad(5).uniform();
            }
            row();
        }
    }

    public boolean isColoured() {
        return coloured;
    }

    public void setColoured(boolean coloured) {
        this.coloured = coloured;
    }
}
