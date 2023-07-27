package server.routes

import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.Route

fun Route.resourceRoute() {
    staticResources("/", "", index = "index.html")
}
