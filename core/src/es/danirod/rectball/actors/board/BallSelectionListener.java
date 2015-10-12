package es.danirod.rectball.actors.board;

import java.util.List;

/**
 * Interface used to trigger events related to ball selection and board.
 * Events such as: a ball has been selected or unselected, or a combination
 * has been made. Possible uses: play a sound when a ball is selected,
 * check if a combination is valid...
 */
public interface BallSelectionListener {

    /**
     * This event is triggered when a ball has been selected on a board.
     * @param ball  ball that has been selected.
     */
    void onBallSelected(BallActor ball);

    /**
     * This event is triggered when a ball has been unselected from a board.
     * @param ball  ball that has been unselected.
     */
    void onBallUnselected(BallActor ball);

    /**
     * This event is triggered when a selection is made and the selection has
     * already be checked to be valid. All the selected balls form a rectangle
     * whose four corners are made of the same color.
     * @param selection  balls that form this selection.
     */
    void onSelectionSucceded(List<BallActor> selection);

    /**
     * This event is triggered when a selection is made and the selection is
     * checked not to be valid. Any of the conditions required to be a valid
     * selection is not happening.
     * @param selection  balls that don't form this selection.
     */
    void onSelectionFailed(List<BallActor> selection);

    /**
     * This event is triggered when the board is forced to unselect every ball.
     * This happens, for instance, when the game is over, so that no selected
     * balls remain on the screen while the animations start.
     * @param selection  the list of balls that has been unselected.
     */
    void onSelectionCleared(List<BallActor> selection);
}
