package org.fasheep.fair.feature.assignment

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun AssignmentScreen(
    modifier: Modifier = Modifier,
    viewModel: AssignmentViewModel = hiltViewModel()
) {
    AssignmentScreenContent()
}

@Composable
internal fun AssignmentScreenContent() {}
