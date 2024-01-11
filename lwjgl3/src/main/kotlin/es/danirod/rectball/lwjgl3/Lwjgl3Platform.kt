package es.danirod.rectball.lwjgl3

import com.badlogic.gdx.Gdx
import es.danirod.rectball.Platform
import es.danirod.rectball.platform.GameServices
import es.danirod.rectball.platform.Marketplace
import es.danirod.rectball.platform.Wakelock
import java.util.Properties

class Lwjgl3Platform : Platform {

    private val versionAssembly by lazy {
        Properties().apply {
            val file = Gdx.files.internal("version.properties")
            if (file.exists()) {
                this.load(file.read())
            }
        }
    }

    override val gameServices = GameServices.NullGameServices()
    override val marketplace = Marketplace.NullMarketplace()
    override val wakelock = Wakelock.NullWakelock()
    override val version: String
        get() = versionAssembly.getProperty("appVersion") ?: "nightly"
    override val buildNumber: Int
        get() = versionAssembly.getProperty("appBuildNumber")?.toInt() ?: 0
}