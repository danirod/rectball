package es.danirod.rectball;

/**
 * Class for holding constant values to keep them centralized.
 */
public class Constants {

    /**
     * Whether the game is running in debug mode or not. Debug mode can be
     * enabled or not during development. Debug mode should be disabled on
     * final game release.
     */
    public static final boolean DEBUG = true;

    /**
     * The width of the Scene2D stage. The bigger this value is, the greater
     * the space is going to be for child elements of this screen. However,
     * the smallest everything is going to be seen, specially in small screen.s
     */
    public static final int VIEWPORT_WIDTH = 500;

    /**
     * The height of the Scene2D stage. The bigger this value is, the greater
     * the space is going to be for child elements of this screen. However,
     * the smallest everything is going to be seen, specially in small screens.
     */
    public static final int VIEWPORT_HEIGHT = 800;

    /**
     * The padding for the table in every stage.
     */
    public static final int STAGE_PADDING = 20;

    public static final int SECONDS = 30;
}
