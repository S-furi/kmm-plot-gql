
import client.graphql.DefaultGraphQLClient
import client.graphql.GraphQLClient
import com.apollographql.apollo3.api.Optional
import gql.client.PointsSubscription
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.dom.clear
import org.jetbrains.letsPlot.Figure
import org.jetbrains.letsPlot.frontend.JsFrontendUtil
import org.jetbrains.letsPlot.geom.geomPoint
import org.jetbrains.letsPlot.geom.geomSmooth
import org.jetbrains.letsPlot.ggsize
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.scale.xlim
import org.jetbrains.letsPlot.scale.ylim

private val apolloClient: GraphQLClient =
    DefaultGraphQLClient.Builder().serverUrl(window.location.origin).addSubscriptionModule().build()

fun main() {
    window.onload = { createContext() }
}

fun createContext() {
    val data = mapOf("x" to mutableListOf<Int>(), "y" to mutableListOf())

    MainScope().launch {
        getData()
            .onEach {
                it.data?.randomPoints?.let { point ->
                    data["x"]?.add(point.x)
                    data["y"]?.add(point.y)
                }
                addPlotToDiv(getPlot(data))
            }
            .onCompletion {
                println("Done Collecting")
                val finalPlot = getPlot(data) + geomSmooth(deg = 3)
                addPlotToDiv(finalPlot)
                // closing the client produces an exception, need to investigate...
                // apolloClient.close()
            }
            .collect()
    }
}

fun addPlotToDiv(p: Figure) {
    document.getElementById("plot")?.apply {
        clear()
        appendChild(JsFrontendUtil.createPlotDiv(p))
    }
}

fun getPlot(data: Map<String, Any>, xx: String = "x", yy: String = "y") =
    letsPlot(data) {
        x = xx
        y = yy
    } +
        geomPoint(color = "red", size = 4.0) +
        xlim(Pair(0, 100)) +
        ylim(Pair(0, 100)) +
        ggsize(700, 350)

fun getData() = apolloClient.subscription(PointsSubscription(Optional.present(10))).toFlow()
