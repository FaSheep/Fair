package org.fasheep.fair.feature.history

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.fasheep.fair.core.model.data.HistoryItem
import java.util.Date

@Composable
internal fun HistoryRoute(
    modifier: Modifier = Modifier,
    hash: String,
    viewModel: HistoryViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val uiState: HistoryUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val index by viewModel.hash.collectAsStateWithLifecycle()
    rememberSaveable(hash) {
        Log.d("HistoryScreen", "HistoryRoute: launch hash=$hash")
        viewModel.onItemClick(hash)
        true
    }
    HistoryScreen(index, uiState, viewModel.refreshing, viewModel::refresh, viewModel::onItemClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen(
    hash: String,
    uiState: HistoryUiState,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onClick: (String) -> Unit
) {
//    val onClick = { str: String -> index = str }
    Surface {
        if (hash.isNotBlank()) AlertDialog(
            onDismissRequest = { onClick("") }, confirmButton = { Text(hash) })
        PullToRefreshBox(isRefreshing = refreshing, onRefresh = onRefresh)
        {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(6.dp)
            ) {
//                item { Text() }
                when (uiState) {
                    is HistoryUiState.Loading -> item { LoadingItem() }
                    is HistoryUiState.Shown -> items(uiState.histories) {
                        when (it) {
                            is HistoryItem.Assignment -> AssignmentHistoryCard(
                                modifier = Modifier.padding(vertical = 5.dp),
                                date = Date(it.blockTimestamp).toString(),
                                num = it.role.size.toString(),
                                onClick = onClick
                            )

                            is HistoryItem.Num -> NumHistoryCard(
                                modifier = Modifier.padding(vertical = 5.dp),
                                date = Date(it.blockTimestamp).toString(),
                                num = it.value,
                                onClick = onClick
                            )
                        }
                    }

                    is HistoryUiState.Empty -> item { Text("Empty") }
                }
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Image(
        imageVector = Icons.Default.Refresh,
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp)
    )
}

@Composable
fun NumHistoryCard(modifier: Modifier = Modifier, date: String, num: String, onClick: (String) -> Unit) {
    Card(modifier = modifier.clickable(onClick = { onClick(num) })) {
        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 20.dp)) {
            Text(
                text = date, modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Text(text = num, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AssignmentHistoryCard(modifier: Modifier = Modifier, date: String, num: String, onClick: (String) -> Unit) {
    Card(modifier = modifier.clickable(onClick = { onClick(num) })) {
        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 20.dp)) {
            Text(
                text = date, modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Text(text = "$num 人", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    val uiState =
        HistoryUiState.Shown(
            listOf(
                HistoryItem.Assignment(1770021251212L, listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Assignment(1730021251212L, listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Assignment(1730021000212L, listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Assignment(1700021000212L, listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Assignment(1700011000212L, listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Assignment(1770011000212L, listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Num(1760021251212L, "123123")
            )
        )
    HistoryScreen(
        "2", uiState, false, {}
    ) {}
}

@Preview
@Composable
fun HistoryCardPreview() {
    NumHistoryCard(modifier = Modifier, "12:00 01/01/2025", "888", {})
}
