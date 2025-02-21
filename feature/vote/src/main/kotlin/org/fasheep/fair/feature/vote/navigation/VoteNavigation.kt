package org.fasheep.fair.feature.vote.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.fasheep.fair.feature.vote.VoteScreen

const val VOTE_ROUTE = "vote_route"

fun NavController.navigateToVote(navOptions: NavOptions) = navigate(VOTE_ROUTE, navOptions)

fun NavGraphBuilder.voteScreen() {
    composable(
        route = VOTE_ROUTE
    ) {
        VoteScreen()
    }
}
