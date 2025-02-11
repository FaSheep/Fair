package org.fasheep.fair.feature.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import org.fasheep.fair.feature.history.navigation.History

class HistoryViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val history = savedStateHandle.toRoute<History>()
}