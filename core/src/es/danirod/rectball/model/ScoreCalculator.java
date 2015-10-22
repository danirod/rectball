package es.danirod.rectball.model;

/**
 * Class whose purpose is calculate the score that will be given to the user.
 *
 * @since 0.4.0
 * @author danirod
 */
public class ScoreCalculator {

    private Bounds bounds;

    public ScoreCalculator(Bounds bounds) {
        this.bounds = bounds;
    }

    public int calculate() {
        int rows = bounds.maxY - bounds.minY + 1;
        int cols = bounds.maxX - bounds.minX + 1;
        return rows * cols;
    }

}
