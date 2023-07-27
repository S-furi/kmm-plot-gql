package server.modules

import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphQLSubscriptionsRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import server.routes.resourceRoute

fun Application.graphQLRoutingModule() {
    routing {
        graphQLGetRoute()
        graphQLPostRoute()
        graphQLSubscriptionsRoute()
        graphiQLRoute()
        graphQLSDLRoute()
        resourceRoute()
    }
}
