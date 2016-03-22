package es.danirod.rectball.screens;

import com.badlogic.gdx.Input;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BackButtonInputProcessorTest {

    @Mock RectballGame game;

    AbstractScreen.BackButtonInputProcessor processor;

    @Before
    public void setUp() {
        processor = new AbstractScreen.BackButtonInputProcessor(game);
        game.player = mock(SoundPlayer.class);
    }

    @Test
    public void shouldReactToEscapeKeys() {
        assertTrue(processor.keyDown(Input.Keys.ESCAPE));
        assertTrue(processor.keyDown(Input.Keys.BACK));
        assertFalse(processor.keyDown(Input.Keys.VOLUME_DOWN));
        assertFalse(processor.keyDown(Input.Keys.HOME));

        assertTrue(processor.keyUp(Input.Keys.ESCAPE));
        assertTrue(processor.keyUp(Input.Keys.BACK));
        assertFalse(processor.keyUp(Input.Keys.VOLUME_DOWN));
        assertFalse(processor.keyUp(Input.Keys.HOME));
    }

    @Test
    public void shouldPlaySoundWhenEscapeKey() {
        processor.keyUp(Input.Keys.ESCAPE);
        processor.keyUp(Input.Keys.VOLUME_DOWN);
        verify(game.player, times(1)).playSound(any(SoundPlayer.SoundCode.class));
    }

    @Test
    public void shouldPopScreenWhenEscapeKey() {
        processor.keyUp(Input.Keys.ESCAPE);
        processor.keyUp(Input.Keys.VOLUME_DOWN);
        verify(game, times(1)).popScreen();
    }

}
