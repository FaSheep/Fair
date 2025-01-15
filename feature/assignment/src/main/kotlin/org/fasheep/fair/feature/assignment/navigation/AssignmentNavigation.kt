package org.fasheep.fair.feature.assignment.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.fasheep.fair.feature.assignment.AssignmentScreen

const val ROOM_ID = "roomId"
const val ASSIGNMENT_ROUTE = "assignment_route/{$ROOM_ID}"

fun NavController.navigateToAssignment(
    roomId: String, navOptions: NavOptions
) {
    navigate("assignment_route/$roomId", navOptions)
    Log.d("TTTTTTTTT", "navigateToAssignment: $roomId")
}

fun NavController.navigateToAssignment(navOptions: NavOptions) = navigate(ASSIGNMENT_ROUTE, navOptions)

fun NavGraphBuilder.assignmentScreen() {
    composable(
        route = ASSIGNMENT_ROUTE,
        arguments = listOf(
            navArgument(ROOM_ID) {
                type = NavType.StringType
                defaultValue = "test"
            },
        )
    ) { backStackEntry ->
        Log.d("TTTT2", "assignmentScreen: ${backStackEntry.arguments?.getString(ROOM_ID)}")
        AssignmentScreen()
    }
}
