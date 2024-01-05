package es.danirod.rectball

import es.danirod.rectball.gameservices.GameServices

interface Platform {

    val gameServices: GameServices

    val marketplace: Marketplace

    val version: String

    val buildNumber: Int

    interface Marketplace {

        val supported: Boolean

        fun open(): Unit

    }

}