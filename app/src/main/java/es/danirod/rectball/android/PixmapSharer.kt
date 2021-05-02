package es.danirod.rectball.android

import android.content.Intent
import android.widget.Toast
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import es.danirod.rectball.Pixmapper
import es.danirod.rectball.RectballGame

class PixmapSharer(private val game: RectballGame) {

    fun share(title: String, message: String, pixmap: Pixmap) {
        try {
            val screenshot = Pixmapper.exportPixmap(game.context, pixmap)
            val intent = game.context.screenshotIntent(screenshot, message)
            game.context.startActivity(Intent.createChooser(intent, title))
        } catch (ex: Exception) {
            Gdx.app.error("PixmapSharer", "Cannot share screenshot", ex)
            game.context.runOnUiThread {
                Toast.makeText(game.context, "Sorry, couldn't share this screenshot!", Toast.LENGTH_LONG).show()
            }
        }
    }

}