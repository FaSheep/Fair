package org.fasheep.fair.feature.vote.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.fasheep.fair.feature.vote.VoteScreen

const val ROOM_ID = "roomId"
const val VOTE_ROUTE = "vote_route/{$ROOM_ID}"

fun NavController.navigateToVote(navOptions: NavOptions) = navigate(VOTE_ROUTE, navOptions)

fun NavGraphBuilder.voteScreen() {
    composable(
        route = VOTE_ROUTE,
        arguments = listOf(
            navArgument(ROOM_ID) { type = NavType.StringType },
        ),
    ) {
        VoteScreen()
    }
}
