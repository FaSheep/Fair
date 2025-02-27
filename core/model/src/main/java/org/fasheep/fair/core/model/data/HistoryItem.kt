package org.fasheep.fair.core.model.data

sealed class HistoryItem {
    abstract val blockTimestamp: Long
    abstract val transactionHash: String

    data class Assignment(
        override val blockTimestamp: Long,
        override val transactionHash: String,
        val name: List<String>,
        val role: List<String>
    ) : HistoryItem()

    data class Num(
        override val blockTimestamp: Long,
        override val transactionHash: String,
        val value: String
    ) : HistoryItem()

    data class Vote(
        override val blockTimestamp: Long,
        override val transactionHash: String,
        val voteId: String,
        val endTime: Long,
        val options: List<String>
    ) : HistoryItem()
}
