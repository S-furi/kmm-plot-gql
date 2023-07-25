package server.schema.subscriptions

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Subscription
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import server.schema.models.Point
import kotlin.random.Random

class PointSubscription : Subscription {
    @GraphQLDescription("Returns a random 2D point every 500 milliseconds")
    fun randomPoints(limit: Int? = null) = flow {
        (0..(limit ?: Int.MAX_VALUE)).forEach { _ ->
            emit(Point(Random.nextInt(100), Random.nextInt(100)))
            delay(500)
        }
    }
}
