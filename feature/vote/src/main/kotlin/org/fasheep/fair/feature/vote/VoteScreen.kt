package org.fasheep.fair.feature.vote

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun VoteScreen(
    modifier: Modifier = Modifier,
    viewModel: VoteViewModel = viewModel()
) {
    Text(text = "B")
}
