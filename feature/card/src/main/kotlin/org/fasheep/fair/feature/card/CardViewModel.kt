package org.fasheep.fair.feature.card

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.fasheep.fair.core.model.data.CardRoom
import org.fasheep.fair.core.model.data.Role
import org.fasheep.fair.core.data.repository.CardRoomRepository
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val cardRoomRepository: CardRoomRepository
) : ViewModel() {

    val rooms =
        cardRoomRepository.findAll().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun add(cardRoom: CardRoom) {
        viewModelScope.launch {
            cardRoomRepository.add(cardRoom, Role())
        }
    }
}
