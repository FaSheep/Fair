package org.fasheep.fair.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val icon: ImageVector,
    val description: String
) {
    CARD(Icons.Default.Add, "card"),
    VOTE(Icons.Default.Search, "vote")
}
