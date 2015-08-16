package es.danirod.rectball.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import es.danirod.rectball.RectballGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 848;
		config.height = 400;
		config.useHDPI = true;
		new LwjglApplication(new RectballGame(), config);
	}
}
