package es.danirod.rectball.model;

/**
 * Class whose purpose is calculate the score that will be given to the user.
 *
 * @since 0.4.0
 * @author danirod
 */
public class ScoreCalculator {

    private CombinationFinder finder;

    private Bounds bounds;

    public ScoreCalculator(Board board, Bounds bounds) {
        this.bounds = bounds;
        this.finder = new CombinationFinder(board);
    }

    public int calculate() {
        int rows = bounds.maxY - bounds.minY + 1;
        int cols = bounds.maxX - bounds.minX + 1;
        int score = rows * cols;

        // Is this the best combination of all the possible ones?
        if (finder.getBestCombination().equals(bounds)) {
            int possibleCombinations = finder.getPossibleBounds().size();
            switch (possibleCombinations) {
                case 1:
                    // For a single one, no bonus is given.
                    break;
                case 2:
                    score = Math.round((float) score * 1.1f);
                    break;
                case 3:
                    score = Math.round((float) score * 1.2f);
                    break;
                default:
                    score = Math.round((float) score * 1.3f);
                    break;
            }
        }

        return score;
    }

}
