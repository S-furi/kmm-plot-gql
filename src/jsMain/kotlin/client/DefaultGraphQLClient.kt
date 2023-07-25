package client

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.ws.GraphQLWsProtocol
import com.apollographql.apollo3.network.ws.WebSocketNetworkTransport

class DefaultGraphQLClient(val client: ApolloClient) : GraphQLClient {
    private var isBuilt: Boolean = false
    private var isSubscriptionModuleEnabled = false

    private constructor(builder: Builder) : this(builder.apolloClientBuilder.build()) {
        isBuilt = true
        isSubscriptionModuleEnabled = builder.isSubscriptionModuleEnabled
    }

    override fun builder(): GraphQLClientBuilder = Builder()

    private fun checkBuilt() = if (!isBuilt) throw ClientNotBuiltException() else true

    class Builder : GraphQLClientBuilder {
        private var url: String? = null

        private var isBuilt: Boolean = false
        override var isSubscriptionModuleEnabled: Boolean = false
        val apolloClientBuilder = ApolloClient.Builder()

        override fun serverUrl(host: String, port: Int): Builder = apply {
            url = "$host:$port"
            apolloClientBuilder.serverUrl("http://$url/graphql")
        }

        override fun addSubscriptionModule(): Builder = apply {
            if (url == null) {
                throw IllegalStateException("You must provide a url before setting up subscription module")
            }
            apolloClientBuilder.subscriptionNetworkTransport(
                WebSocketNetworkTransport.Builder()
                    .serverUrl("ws://$url/subscriptions")
                    // specifying "graphql-ws" protocol
                    .protocol(GraphQLWsProtocol.Factory())
                    .build(),
            )

            isSubscriptionModuleEnabled = true
        }

        override fun build(): DefaultGraphQLClient {
            isBuilt = true
            return DefaultGraphQLClient(this)
        }
    }
}

class ClientNotBuiltException :
    Exception("The client was not built properly! Make sure to call `build()` before running any operation.")
