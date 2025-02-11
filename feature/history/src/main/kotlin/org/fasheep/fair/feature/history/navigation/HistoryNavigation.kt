package org.fasheep.fair.feature.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.fasheep.fair.feature.history.HistoryRoute

@Serializable
data class History(val hash: String)

fun NavController.navigateToHistory(navOptions: NavOptions, hash: String = "") = navigate(History(hash), navOptions)

fun NavGraphBuilder.historyScreen() {
    composable<History> {
        HistoryRoute()
    }
}
