package org.fasheep.fair.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import org.fasheep.fair.feature.card.navigation.navigateToCard
import org.fasheep.fair.feature.vote.navigation.navigateToVote
import org.fasheep.fair.navigation.TopLevelDestination

@Composable
fun rememberFairAppState(
    navController: NavHostController = rememberNavController()
): FairAppState {
    return remember(
        navController
    ) {
        FairAppState(
            navController = navController
        )
    }
}

@Stable
class FairAppState(
    val navController: NavHostController
) {
    val currentRoute
        @Composable
        get() = navController.currentBackStackEntryAsState().value?.destination

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.CARD -> navController.navigateToCard("222", topLevelNavOptions)
            TopLevelDestination.VOTE -> navController.navigateToVote(topLevelNavOptions)
        }
    }

}
