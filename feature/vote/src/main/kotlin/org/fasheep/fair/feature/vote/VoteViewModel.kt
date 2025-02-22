package org.fasheep.fair.feature.vote

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val TAG = "VoteVM"

class VoteViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    fun createVote(endTime: Long, options: List<String>) {
        Log.d(TAG, "endTime: $endTime\noptions: $options")
    }
}