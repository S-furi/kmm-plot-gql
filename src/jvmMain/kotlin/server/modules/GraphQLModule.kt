package server.modules

import com.expediagroup.graphql.dataloader.KotlinDataLoaderRegistryFactory
import com.expediagroup.graphql.generator.hooks.FlowSubscriptionSchemaGeneratorHooks
import com.expediagroup.graphql.server.ktor.DefaultKtorGraphQLContextFactory
import com.expediagroup.graphql.server.ktor.GraphQL
import io.ktor.serialization.jackson.JacksonWebsocketContentConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.websocket.WebSockets
import server.schema.queries.TestQuery
import server.schema.subscriptions.PointSubscription

fun Application.graphQLModule() {
    install(WebSockets) {
        pingPeriodMillis = 1000
        contentConverter = JacksonWebsocketContentConverter()
    }

    install(CORS) {
        anyHost() // to remove in production.
    }

    install(Compression) {
        gzip()
    }

    install(GraphQL) {
        schema {
            packages = listOf("server")
            queries = listOf(TestQuery())
            mutations = emptyList()
            subscriptions = listOf(PointSubscription())
            hooks = FlowSubscriptionSchemaGeneratorHooks()
        }
        engine {
            dataLoaderRegistryFactory = KotlinDataLoaderRegistryFactory(
                // Insert here custom data loaders
            )
        }
        server {
            contextFactory = DefaultKtorGraphQLContextFactory()
        }
    }
}
