package org.fasheep.fair.feature.homepage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.fasheep.fair.feature.homepage.HomepageScreen

@Serializable
data object HomepageRoute

fun NavController.navigateToHomepage(navOptions: NavOptions) = navigate(HomepageRoute, navOptions)

fun NavGraphBuilder.homepageScreen() {
    composable<HomepageRoute> {
        HomepageScreen()
    }
}
