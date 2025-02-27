package org.fasheep.fair.feature.vote.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.fasheep.fair.feature.vote.VoteRoute

const val VOTE_ROUTE = "vote_route"

fun NavController.navigateToVote(navOptions: NavOptions) = navigate(VOTE_ROUTE, navOptions)

fun NavGraphBuilder.voteScreen(onNavHistory: (String) -> Unit) {
    composable(
        route = VOTE_ROUTE
    ) {
        VoteRoute(onNavHistory = onNavHistory)
    }
}
