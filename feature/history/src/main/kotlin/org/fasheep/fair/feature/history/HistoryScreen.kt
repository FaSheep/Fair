package org.fasheep.fair.feature.history

import android.icu.text.SimpleDateFormat
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.delay
import org.fasheep.fair.core.model.data.DataDetail
import org.fasheep.fair.core.model.data.HistoryItem
import java.util.Date

@Composable
internal fun HistoryRoute(
    modifier: Modifier = Modifier,
    hash: String,
    viewModel: HistoryViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val uiState: HistoryUiState by viewModel.uiState.collectAsState()
    val details = viewModel.details
    var index by rememberSaveable(hash) { mutableStateOf(hash) }
    val connect by viewModel.connect.collectAsState()
    LaunchedEffect(connect) {
        delay(1000)
        viewModel.checkConnect()
    }
    HistoryScreen(
        index,
        uiState,
        details,
        viewModel.refreshing,
        viewModel::refresh,
        onClick = { index = it },
        viewModel::fetchDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen(
    hash: String,
    uiState: HistoryUiState,
    details: List<DataDetail>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onClick: (String) -> Unit,
    onFetchDetail: (HistoryItem) -> Unit
) {
    Surface {
        if (hash.isNotBlank()) HistoryDialog(
            uiState = uiState,
            details = details,
            hash = hash,
            onFetchDetail = onFetchDetail,
            onClick = onClick
        )
        if (uiState is HistoryUiState.Loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.2f))
            }
        } else {
            PullToRefreshBox(isRefreshing = refreshing, onRefresh = onRefresh) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (uiState is HistoryUiState.Shown) {
                        items(uiState.histories) {
                            HistoryCard(
                                modifier = Modifier.padding(vertical = 5.dp),
                                historyItem = it
                            ) { onClick(it.transactionHash) }
                        }
                    } else {
                        item { Text("Empty") }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryDialog(
    uiState: HistoryUiState,
    details: List<DataDetail>,
    hash: String,
    onFetchDetail: (HistoryItem) -> Unit,
    onClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var showMore by remember { mutableStateOf(false) }
    if (uiState !is HistoryUiState.Shown) return
    val historyItem = uiState.histories.find { it.transactionHash == hash }
    val detail = details.find { it.hash == hash }
    AlertDialog(
        text = {
            when (historyItem) {
                is HistoryItem.Assignment -> Text(
                    """
                        Hash: ${historyItem.transactionHash}
                        
                        Time: ${SimpleDateFormat.getInstance().format(Date(historyItem.blockTimestamp))}
                        
                        Data: ${historyItem.name.zip(historyItem.role)}
                    """.trimIndent()
                )

                is HistoryItem.Num -> Text(
                    """
                        Hash: ${historyItem.transactionHash}
                        
                        Time: ${SimpleDateFormat.getInstance().format(Date(historyItem.blockTimestamp))}
                        
                        Data: ${historyItem.value} (${historyItem.min}-${historyItem.max})
                    """.trimIndent()
                )

                is HistoryItem.Vote -> Column(Modifier.verticalScroll(scrollState)) {
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Image(
                            bitmap = BarcodeEncoder().encodeBitmap(
                                historyItem.voteId,
                                BarcodeFormat.QR_CODE,
                                400,
                                400
                            ).asImageBitmap(), contentDescription = "QR Code"
                        )
                    }
                    Text(
                        """
                            Hash: ${historyItem.transactionHash}
                            
                            Time: ${SimpleDateFormat.getInstance().format(Date(historyItem.blockTimestamp))}
                            
                            End Time: ${SimpleDateFormat.getInstance().format(Date(historyItem.endTime * 1000))}
                            
                            Vote ID: ${historyItem.voteId}
                            
                            Options: ${historyItem.options}
                        """.trimIndent()
                    )
                    if (showMore) {
                        if (detail == null) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 20.dp)
                            )
                            LaunchedEffect(historyItem.voteId) {
                                onFetchDetail(historyItem)
                            }
                        } else if (detail is DataDetail.Vote) {
                            Text(
                                """
                                    
                                    Data: ${detail.data}
                                """.trimIndent()
                            )
                        }
                    }
                }

                null -> Text("null")
            }
        },
        onDismissRequest = { onClick("") },
        dismissButton = {
            if (!showMore && historyItem is HistoryItem.Vote) {
                TextButton(onClick = { showMore = true }) { Text("Show more") }
            }
        },
        confirmButton = { TextButton(onClick = { onClick("") }) { Text("OK") } }
    )
}

@Composable
fun HistoryCard(modifier: Modifier = Modifier, historyItem: HistoryItem, onClick: () -> Unit) {
    Card(modifier = modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = SimpleDateFormat.getInstance().format(Date(historyItem.blockTimestamp))
            )
            Spacer(Modifier.weight(1f))
            when (historyItem) {
                is HistoryItem.Assignment -> Text(
                    text = "${historyItem.role.size}人",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                is HistoryItem.Num -> Text(
                    text = historyItem.value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                is HistoryItem.Vote -> Text(
                    text = "${historyItem.options.size}选项",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    val uiState =
        HistoryUiState.Shown(
            listOf(
                HistoryItem.Assignment(1770021251212L, "a", listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Assignment(1730021251212L, "a", listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Assignment(1730021000212L, "a", listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Assignment(1700021000212L, "a", listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Assignment(1700011000212L, "a", listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Assignment(1770011000212L, "a", listOf("a", "b"), listOf("A", "B")),
                HistoryItem.Num(1760021251212L, "123123", "99999999999999999999", "1", "2")
            )
        )
    HistoryScreen(
        "123123", uiState, emptyList(), false, {}, {}
    ) {}
}

@Preview
@Composable
fun LoadingHistoryScreenPreview() {
    val uiState =
        HistoryUiState.Loading
    HistoryScreen(
        "", uiState, emptyList(), false, {}, {}
    ) {}
}

@Preview
@Composable
fun CardPreview() {
    HistoryCard(modifier = Modifier, HistoryItem.Num(1766666666666L, "hash", "222", "1", "100"), {})
}
