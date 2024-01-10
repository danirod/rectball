package es.makigas.gdxhola.lwjgl3

import es.danirod.rectball.Platform
import es.danirod.rectball.gameservices.GameServices

class Lwjgl3Platform : Platform {
    override val gameServices: GameServices = object : GameServices {
        override val supported = false

        override fun signIn() {

        }

        override fun signOut() {

        }

        override val isLoggingIn = false
        override val isSignedIn = false

        override fun uploadScore(score: Int, time: Int) {

        }

        override fun showLeaderboards() {

        }

        override fun showAchievements() {

        }

    }
    override val marketplace = object : Platform.Marketplace {
        override val supported = false

        override fun open() {

        }

    }
    override val version = "0.5"
    override val buildNumber = 437
}
