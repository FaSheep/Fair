package org.fasheep.fair.core.model.data

sealed class HistoryItem {
    abstract val blockTimestamp: Long

    data class Assignment(
        override val blockTimestamp: Long,
        val name: List<String>,
        val role: List<String>
    ) : HistoryItem()

    data class Num(
        override val blockTimestamp: Long,
        val value: String
    ) : HistoryItem()
}
