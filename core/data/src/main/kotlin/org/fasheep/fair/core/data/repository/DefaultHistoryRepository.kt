package org.fasheep.fair.core.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import org.fasheep.fair.core.model.data.HistoryItem
import org.fasheep.fair.core.network.GraphRepository
import javax.inject.Inject
import javax.inject.Singleton

const val TAG = "HistoryRepository"

@Singleton
internal class DefaultHistoryRepository @Inject constructor(
    private val graphRepository: GraphRepository
) : HistoryRepository {
    private val needRefresh = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        Log.d(TAG, "init: init")
    }

    override fun observeHistories(address: String): Flow<List<HistoryItem>> =
        needRefresh.onStart { emit(Unit) }.mapLatest {
            graphRepository.findNumByAddress(address)
                .map { item -> HistoryItem.Num(item.timestamp, item.value) }
        }

    override fun update() {
        needRefresh.tryEmit(Unit)
    }
}
