package org.fasheep.fair.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import org.fasheep.fair.feature.assignment.navigation.navigateToAssignment
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
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val showNavBar: Boolean
        @Composable get() {
            return TopLevelDestination.entries.any { topLevelDestination ->
                currentDestination?.route?.equals(topLevelDestination.route.qualifiedName) == true
            }
        }

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
            TopLevelDestination.ASSIGNMENT -> navController.navigateToAssignment(topLevelNavOptions)
//            TopLevelDestination.VOTE -> navController.navigateToVote(topLevelNavOptions)
//            TopLevelDestination.SORTITION -> navController.navigateToSortition(topLevelNavOptions)
//            TopLevelDestination.HISTORY -> navController.navigateToHistory(navOptions = topLevelNavOptions)
        }
    }

}
