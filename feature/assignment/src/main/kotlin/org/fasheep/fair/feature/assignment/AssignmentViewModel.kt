package org.fasheep.fair.feature.assignment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.fasheep.fair.core.data.repository.HistoryRepository
import javax.inject.Inject

const val TAG = "AssignmentVM"

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {
}
