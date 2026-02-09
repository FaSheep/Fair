package org.fasheep.fair.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import org.fasheep.fair.feature.homepage.navigation.HomepageRoute
import org.fasheep.fair.feature.homepage.navigation.homepageScreen
import org.fasheep.fair.navigation.TopLevelDestination

@Composable
fun FairApp(appState: FairAppState) {
    Scaffold(bottomBar = {
        NavigationBar {
            TopLevelDestination.entries.forEach {
                NavigationBarItem(
                    selected = appState.currentDestination.isTopLevelDestinationInHierarchy(it),
                    onClick = { appState.navigateToTopLevelDestination(it) },
                    icon = {
                        Icon(
                            painter = painterResource(it.icon), contentDescription =
                                stringResource(it.descriptionTextId)
                        )
                    })
            }
        }
    }) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = appState.navController,
            startDestination = HomepageRoute
        ) {
            homepageScreen()
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
