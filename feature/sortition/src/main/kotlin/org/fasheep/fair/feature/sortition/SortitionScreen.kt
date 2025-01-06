package org.fasheep.fair.feature.sortition

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun SortitionScreen(
    modifier: Modifier = Modifier,
    viewModel: SortitionViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isConnected.collectAsState(false)
    val selectAddress by viewModel.selectAddress.collectAsState("0x0")

    Surface {
        Column {
            Spacer(modifier = modifier.weight(1f))
            Text("$isConnected:$selectAddress")
            Spacer(modifier = modifier.weight(1f))
            Button(onClick = {
                viewModel.connect()
            }) { Text("Connect") }
        }
    }
}
