package es.danirod.rectball.model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CombinationFinderTest {

    @Test
    public void testOnePossibleBounds() {
        Board board = setUpBoard(new char[][] {
                { 'Y', 'B', 'R', 'Y', 'B', 'Y' },
                { 'R', 'G', 'Y', 'G', 'Y', 'R' },
                { 'Y', 'Y', 'R', 'R', 'B', 'B' },
                { 'R', 'G', 'R', 'R', 'B', 'Y' },
                { 'Y', 'R', 'B', 'R', 'Y', 'R' },
                { 'B', 'R', 'R', 'Y', 'R', 'B' }
        });
        CombinationFinder finder = new CombinationFinder(board);
        assertEquals(new Bounds(2, 2, 3, 3), finder.getCombination());
        assertEquals(1, finder.getPossibleBounds().size());

        assertEquals(new Bounds(2, 2, 3, 3), finder.getBestCombination());
        assertEquals(new Bounds(2, 2, 3, 3), finder.getWorstCombination());
    }

    @Test
    public void testTwoPossibleBounds() {
        Board board = setUpBoard(new char[][] {
                { 'B', 'R', 'B', 'R', 'Y', 'Y' },
                { 'R', 'Y', 'R', 'G', 'B', 'Y' },
                { 'B', 'G', 'B', 'G', 'R', 'G' },
                { 'Y', 'B', 'G', 'Y', 'G', 'R' },
                { 'B', 'R', 'G', 'B', 'Y', 'B' },
                { 'G', 'R', 'B', 'G', 'R', 'G' }
        });
        CombinationFinder finder = new CombinationFinder(board);
        assertEquals(2, finder.getPossibleBounds().size());

        Bounds bounds1 = new Bounds(0, 0, 2, 2);
        Bounds bounds2 = new Bounds(3, 2, 5, 5);
        assertTrue(finder.getPossibleBounds().contains(bounds1));
        assertTrue(finder.getPossibleBounds().contains(bounds2));

        assertEquals(bounds1, finder.getWorstCombination());
        assertEquals(bounds2, finder.getBestCombination());
    }

    @Test
    public void testThreePossibleBounds() {
        Board board = setUpBoard(new char[][] {
                { 'B', 'R', 'B', 'Y', 'Y', 'Y' },
                { 'R', 'Y', 'R', 'Y', 'B', 'Y' },
                { 'B', 'G', 'B', 'G', 'R', 'G' },
                { 'Y', 'B', 'G', 'Y', 'G', 'R' },
                { 'B', 'R', 'G', 'B', 'Y', 'B' },
                { 'G', 'R', 'B', 'G', 'R', 'G' }
        });
        CombinationFinder finder = new CombinationFinder(board);
        assertEquals(3, finder.getPossibleBounds().size());

        Bounds bounds1 = new Bounds(0, 0, 2, 2);
        Bounds bounds2 = new Bounds(3, 2, 5, 5);
        Bounds bounds3 = new Bounds(3, 0, 5, 1);

        assertTrue(finder.getPossibleBounds().contains(bounds1));
        assertTrue(finder.getPossibleBounds().contains(bounds2));
        assertTrue(finder.getPossibleBounds().contains(bounds3));
        assertTrue(finder.getPossibleBounds().contains(bounds1));
        assertTrue(finder.getPossibleBounds().contains(bounds2));
        assertEquals(bounds3, finder.getWorstCombination());
        assertEquals(bounds2, finder.getBestCombination());
    }

    @Test
    public void testBoundsInBounds() {
        Board board = setUpBoard(new char[][] {
                { 'Y', 'B', 'R', 'Y', 'B', 'Y' },
                { 'R', 'G', 'Y', 'G', 'Y', 'R' },
                { 'Y', 'Y', 'R', 'R', 'R', 'B' },
                { 'R', 'G', 'R', 'R', 'R', 'Y' },
                { 'Y', 'R', 'B', 'R', 'Y', 'R' },
                { 'B', 'R', 'B', 'Y', 'R', 'B' }
        });
        CombinationFinder finder = new CombinationFinder(board);

        /*
         * We have a RRR - RRR combination in (2,2 -> 4,3). This is actually
         * three combinations: (2,2 -> 4,3), (2,2 -> 3,3) and (3,2 -> 4,3).
         */
        assertEquals(3, finder.getPossibleBounds().size());

        Bounds bigOne = new Bounds(2, 2, 4, 3);
        Bounds leftOne = new Bounds(2, 2, 3, 3);
        Bounds rightOne = new Bounds(3, 2, 4, 3);

        assertTrue(finder.getPossibleBounds().contains(bigOne));
        assertTrue(finder.getPossibleBounds().contains(leftOne));
        assertTrue(finder.getPossibleBounds().contains(rightOne));

        assertEquals(new Bounds(2, 2, 4, 3), finder.getBestCombination());
        assertTrue(finder.getWorstCombination().equals(leftOne) ||
                    finder.getWorstCombination().equals(rightOne));
    }

    private Board setUpBoard(char[][] chars) {
        if (chars.length != chars[0].length) {
            throw new IllegalArgumentException("Not a square.");
        }

        Board board = new Board(chars.length);
        for (int y = 0; y < chars.length; y++) {
            for (int x = 0; x < chars.length; x++) {
                switch (chars[y][x]) {
                    case 'Y':
                        board.getBall(x, y).setColor(BallColor.YELLOW);
                        break;
                    case 'R':
                        board.getBall(x, y).setColor(BallColor.RED);
                        break;
                    case 'G':
                        board.getBall(x, y).setColor(BallColor.GREEN);
                        break;
                    case 'B':
                        board.getBall(x, y).setColor(BallColor.BLUE);
                        break;
                }
            }
        }
        return board;
    }
}
