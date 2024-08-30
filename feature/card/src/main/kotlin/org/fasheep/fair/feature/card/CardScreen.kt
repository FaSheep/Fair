package org.fasheep.fair.feature.card

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.fasheep.fair.core.model.data.CardRoom

@Composable
internal fun CardScreen(
    modifier: Modifier = Modifier,
    viewModel: CardViewModel = hiltViewModel()
) {
    remember {
        CoroutineScope(Dispatchers.Default).launch {
            for (i in 1..3) {
                delay(1000)
                viewModel.add(CardRoom(iconPath = "$i", name = "$i"))
            }
        }
    }
    val rooms by viewModel.rooms.collectAsStateWithLifecycle(initialValue = emptyList())

    LazyColumn (modifier = modifier.fillMaxSize()){
        items(rooms) {
            Card {
                Text(text = "${it.id}")
            }
        }
    }
}
