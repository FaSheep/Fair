package org.fasheep.fair.feature.assignment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.fasheep.fair.feature.assignment.AssignmentScreen

@Serializable
data object AssignmentRoute

fun NavController.navigateToAssignment(navOptions: NavOptions) = navigate(AssignmentRoute, navOptions)

fun NavGraphBuilder.assignmentScreen() {
    composable<AssignmentRoute> {
        AssignmentScreen()
    }
}
