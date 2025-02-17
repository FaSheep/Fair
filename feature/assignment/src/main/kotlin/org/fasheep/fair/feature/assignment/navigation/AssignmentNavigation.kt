package org.fasheep.fair.feature.assignment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.fasheep.fair.feature.assignment.AssignmentRoute

const val ASSIGNMENT_ROUTE = "assignment_route"

fun NavController.navigateToAssignment(navOptions: NavOptions) = navigate(ASSIGNMENT_ROUTE, navOptions)

fun NavGraphBuilder.assignmentScreen(callback: (String) -> Unit) {
    composable(
        route = ASSIGNMENT_ROUTE
    ) {
        AssignmentRoute(callback = callback)
    }
}
