package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.backends.headless.mock.input.MockInput;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.danirod.rectball.RectballGame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractScreenTest {

    @Mock RectballGame fakeGame;

    @Mock Viewport fakeViewport;

    AbstractScreen screen;

    @Before
    public void setUp() throws Exception {
        /* Mocked Batch. */
        Batch batch = mock(SpriteBatch.class);
        when(fakeGame.getBatch()).thenReturn(batch);

        /* Mocked Viewport. Will fail because Headless GDX doesn't have natives. */
        doNothing().when(fakeViewport).update(anyInt(), anyInt(), anyBoolean());

        /* Mocked libGDX. */
        Gdx.graphics = new MockGraphics();
        Gdx.files = new HeadlessFiles();
        Gdx.input = new MockInput();

        /* Tested screen. */
        screen = new AbstractScreen(fakeGame) {
            @Override public int getID() { return -1; }
            @Override void setUpInterface(Table table) { }
            @Override Viewport buildViewport() { return fakeViewport; }
        };
    }

    @Test
    public void gameInstanceShouldBeAccessible() {
        assertSame(fakeGame, screen.game);
    }

    @Test
    public void screenIDShouldBeAccessible() {
        assertEquals(-1, screen.getID());
    }

    @Test
    public void inputProcessorShouldBeSet() {
        Gdx.input = mock(Input.class);
        screen.show();
        verify(Gdx.input).setInputProcessor(any(InputProcessor.class));
    }

    @Test
    public void inputProcessorShouldBeUnset() {
        Gdx.input = mock(Input.class);
        screen.hide();
        verify(Gdx.input).setInputProcessor(null);
    }

    /**
     * When the resize() method is executed, the actual viewport should be resized.
     */
    @Test public void viewportShouldBeResized() {
        Viewport viewport = mock(Viewport.class);
        Stage stage = mock(Stage.class);
        when(stage.getViewport()).thenReturn(viewport);
        screen.stage = stage;
        screen.resize(400, 400);
        verify(viewport).update(400, 400);
    }

    /**
     * When rendering a frame, the entire screen should be cleared to prevent glitches.
     */
    @Test public void screenShouldBeCleared() {
        Gdx.gl = mock(GL20.class);
        screen.stage = mock(Stage.class);
        screen.render(0);
        verify(Gdx.gl).glClear(GL20.GL_COLOR_BUFFER_BIT);
        verify(Gdx.gl).glClearColor(anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    /**
     * The stage instance used in getStage() should be the valid one.
     */
    @Test public void stageShouldBeAccessible() {
        screen.stage = mock(Stage.class);
        assertSame(screen.stage, screen.getStage());
    }

    /**
     * The stage should be used to render things in the screen.
     */
    @Test public void stageShouldBeRendered() {
        Gdx.gl = mock(GL20.class);
        screen.stage = mock(Stage.class);
        screen.render(0);
        verify(screen.stage).act();
        verify(screen.stage).draw();
    }

    /**
     * When initializing the screen, a table should be built.
     */
    @Test public void tableShouldBeSetOnShow() {
        screen.table = null;
        screen.show();
        assertNotNull(screen.table);
    }

    /**
     * Table should persist against hide(), so calling show() on a previously hidden screen
     * should reuse the same table without instantiating no new tables to avoid holes in GC.
     */
    @Test public void tableShouldBeReutilized() {
        screen.table = mock(Table.class);
        screen.show();
        verify(screen.table).clear();
    }

    /**
     * When the screen is disposed, everything with it should dissapear.
     */
    @Test public void resourcesShouldBeDisposed() {
        Stage stage = mock(Stage.class);
        screen.table = mock(Table.class);
        screen.stage = stage;
        screen.dispose();
        verify(stage).dispose();
        assertNull(screen.table);
        assertNull(screen.stage);
    }

    /**
     * If the Back button should not be handled, then the InputProcessor for the screen
     * should be the stage itself.
     */
    @Test public void whenBackIsNotHandledShouldUseStageForInput() {
        AbstractScreen screen = new AbstractScreen(fakeGame, false) {
            @Override public int getID() { return -1; }
            @Override void setUpInterface(Table table) { }
            @Override Viewport buildViewport() { return fakeViewport; }
        };
        Gdx.input = mock(Input.class);
        screen.show();
        verify(Gdx.input).setInputProcessor(screen.stage);
        assertFalse(screen.handleBack);
    }

    /**
     * If the handheld device is on landscape orientation (horizontal), the viewport should
     * be rectangular to make it easy to play on tablet devices and PC displays.
     */
    @Test public void landscapeViewportShouldBeSquare() {
        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getWidth()).thenReturn(320);
        when(Gdx.graphics.getHeight()).thenReturn(240);
        Viewport v = screen.buildViewport();
        assertEquals(v.getWorldWidth(), v.getWorldHeight(), 0.0);
    }

    /**
     * If the handheld device is on portrait orientation (vertical), the viewport should be as tall
     * as possible to fit the device height.
     */
    @Test public void portraitViewportShouldBeVertical() {
        screen = new AbstractScreen(fakeGame) {
            @Override public int getID() { return -1; }
            @Override void setUpInterface(Table table) { }
        };
        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getWidth()).thenReturn(240);
        when(Gdx.graphics.getHeight()).thenReturn(320);
        Viewport v = screen.buildViewport();
        assertTrue(v.getWorldWidth() < v.getWorldHeight());
    }
}
