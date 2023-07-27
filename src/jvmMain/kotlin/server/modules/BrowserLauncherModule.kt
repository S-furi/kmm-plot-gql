package server.modules

import io.ktor.server.engine.ApplicationEngineEnvironment
import java.awt.Desktop
import java.net.URI

fun ApplicationEngineEnvironment.browserLauncherModule() {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        connectors.forEach {
            // little workaround of address resolution in local browser (safari)
            val host = if (it.host == "0.0.0.0") "localhost" else it.host
            Desktop.getDesktop().browse(URI("${it.type.name.lowercase()}://$host:${it.port}"))
        }
    }
}
