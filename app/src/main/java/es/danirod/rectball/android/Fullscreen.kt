package es.danirod.rectball.android

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.badlogic.gdx.Gdx

class Fullscreen(private val activity: Activity) {

    interface FullscreenImplementation {
        fun onEnterFullscreen()
        fun onLeaveFullscreen()
    }

    private val window = activity.window

    private val implementation: FullscreenImplementation = Build.VERSION.SDK_INT.let {
        when {
            it >= 19 -> KitkatScreenImplementation()
            it >= 16 -> JellybeanScreenImplementation()
            it >= 14 -> IceCreamScreenImplementation()
            else -> NoopScreenImplementation()
        }
    }

    fun onEnterFullscreen() = implementation.onEnterFullscreen()

    fun onLeaveFullscreen() = implementation.onLeaveFullscreen()

    @TargetApi(19)
    @Suppress("DEPRECATION")
    inner class KitkatScreenImplementation : FullscreenImplementation {

        override fun onEnterFullscreen() {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        override fun onLeaveFullscreen() {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    @TargetApi(16)
    @Suppress("DEPRECATION")
    inner class JellybeanScreenImplementation : FullscreenImplementation {
        override fun onEnterFullscreen() {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
        }

        override fun onLeaveFullscreen() {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    @Suppress("DEPRECATION")
    inner class IceCreamScreenImplementation : FullscreenImplementation {
        override fun onEnterFullscreen() {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }

        override fun onLeaveFullscreen() {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    inner class NoopScreenImplementation : FullscreenImplementation {
        override fun onEnterFullscreen() = Gdx.app.log("Fullscreen", "onEnterFullscreen")
        override fun onLeaveFullscreen() = Gdx.app.log("Fullscreen", "onLeaveFullscreen")
    }
}