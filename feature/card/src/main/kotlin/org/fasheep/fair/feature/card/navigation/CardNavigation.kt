package org.fasheep.fair.feature.card.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.fasheep.fair.feature.card.CardScreen

const val ROOM_ID = "roomId"
const val CARD_ROUTE = "card_route/{$ROOM_ID}"

fun NavController.navigateToCard(
    roomId: String, navOptions: NavOptions
) {
    navigate("card_route/$roomId", navOptions)
    Log.d("TTTTTTTTT", "navigateToCard: $roomId")
}

fun NavController.navigateToCard(navOptions: NavOptions) = navigate(CARD_ROUTE, navOptions)

fun NavGraphBuilder.cardScreen() {
    composable(
        route = CARD_ROUTE,
        arguments = listOf(
            navArgument(ROOM_ID) {
                type = NavType.StringType
                defaultValue = "test"
            },
        )
    ) { backStackEntry ->
        Log.d("TTTT2", "cardScreen: ${backStackEntry.arguments?.getString(ROOM_ID)}")
        CardScreen()
    }
}
