package org.fasheep.fair.feature.homepage

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun HomepageScreen(
    modifier: Modifier = Modifier,
    viewModel: HomepageViewModel = hiltViewModel()
) {
    HomepageScreenContent()
}

@Composable
internal fun HomepageScreenContent() {}
