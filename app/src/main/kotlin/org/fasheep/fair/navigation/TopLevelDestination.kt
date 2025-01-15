package org.fasheep.fair.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val icon: ImageVector,
    val description: String
) {
    ASSIGNMENT(Icons.Default.Add, "assignment"),
    VOTE(Icons.Default.Search, "vote"),
    SORTITION(Icons.Default.Done, "sortition")
}
