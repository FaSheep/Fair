package org.fasheep.fair.feature.sortition.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.fasheep.fair.feature.sortition.SortitionScreen

const val SORTITION_ROUTE = "sortition_route"
const val TAG = "SortitionNavigation"

fun NavController.navigateToSortition(navOptions: NavOptions) = navigate(SORTITION_ROUTE, navOptions)

fun NavGraphBuilder.sortitionScreen() {
    composable(
        route = SORTITION_ROUTE
    ) {
        SortitionScreen()
    }
}
