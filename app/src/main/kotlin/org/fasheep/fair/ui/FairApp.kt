package org.fasheep.fair.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import org.fasheep.fair.feature.card.navigation.CARD_ROUTE
import org.fasheep.fair.feature.card.navigation.cardScreen
import org.fasheep.fair.feature.vote.navigation.voteScreen
import org.fasheep.fair.navigation.TopLevelDestination

@Composable
fun FairApp(appState: FairAppState) {
    Scaffold(bottomBar = {
        NavigationBar {
            TopLevelDestination.entries.forEach {
                NavigationBarItem(
                    selected = appState.currentRoute.isTopLevelDestinationInHierarchy(it),
                    onClick = { appState.navigateToTopLevelDestination(it) },
                    icon = { Icon(imageVector = it.icon, contentDescription = it.description) })
            }
        }
    }) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = appState.navController,
            startDestination = CARD_ROUTE
        ) {
            cardScreen()
            voteScreen()
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
