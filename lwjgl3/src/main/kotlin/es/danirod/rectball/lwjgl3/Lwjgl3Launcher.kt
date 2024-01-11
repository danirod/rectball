@file:JvmName("Lwjgl3Launcher")
package es.danirod.rectball.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import es.danirod.rectball.RectballGame

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired())
        return
    Lwjgl3Application(RectballGame(Lwjgl3Platform()), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Rectball")
        setResizable(false)
        setWindowedMode(480, 720)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
