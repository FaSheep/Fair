package org.fasheep.fair.feature.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.fasheep.fair.feature.history.HistoryRoute

@Serializable
data class History(val hash: String)

fun NavController.navigateToHistory(
    hash: String = "", navOptions: NavOptions? = navOptions {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(this@navigateToHistory.graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
    }
) = navigate(History(hash), navOptions)

fun NavGraphBuilder.historyScreen() {
    composable<History> {
        HistoryRoute(hash = it.toRoute<History>().hash)
    }
}
