package org.fasheep.fair.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.fasheep.fair.core.model.data.HistoryItem

interface HistoryRepository {
    fun observeHistories(address: String): Flow<List<HistoryItem>>
    fun update()
}
