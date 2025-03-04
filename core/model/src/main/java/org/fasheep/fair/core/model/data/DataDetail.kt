package org.fasheep.fair.core.model.data

sealed class DataDetail {
    abstract val hash: String

    data class Vote(
        override val hash: String,
        val voteId: String,
        val options: List<String>,
        val data: List<VoteCast>
    ) : DataDetail()
}



