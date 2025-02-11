package org.fasheep.fair.feature.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun HistoryRoute(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    AssignmentScreen(
    )
}

@Composable
internal fun AssignmentScreen() {
}