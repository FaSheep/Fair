package org.fasheep.fair.feature.sortition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun SortitionRoute(
    modifier: Modifier = Modifier,
    callback: (String) -> Unit,
    viewModel: SortitionViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle(false)
    val num by viewModel.num.collectAsStateWithLifecycle("N/A")
    SortitionScreen(
        modifier = modifier,
        isConnected = isConnected,
        num = num,
        onConnect = viewModel::connect,
        onTran = viewModel::tranRand,
        callback = callback
    )
}

@Composable
internal fun SortitionScreen(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    num: String,
    onConnect: () -> Unit,
    onTran: ((String) -> Unit) -> Unit,
    callback: (String) -> Unit
) {
    var onlineMode: Boolean by remember { mutableStateOf(isConnected) }
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 100.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                fontSize = 100.sp,
                textAlign = TextAlign.Center,
                text = num
            )
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                label = {
                    Text("Max Value")
                },
            )
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                label = {
                    Text("Min Value")
                }
            )
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(checked = onlineMode,
                    onCheckedChange = {
                        if (it) {
                            if (isConnected) {
                                onlineMode = true
                            } else {
                                onConnect()
                            }
                        } else {
                            onlineMode = false
                        }
                    })
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp), text = "Online Mode"
                )
                Button(onClick = { if (onlineMode) onTran(callback) else onlineMode = true }) { Text("Tran") }
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun Preview() {
    SortitionScreen(
        modifier = Modifier,
        isConnected = false,
        num = "N/A",
        onConnect = {},
        onTran = {},
        callback = {}
    )
}
