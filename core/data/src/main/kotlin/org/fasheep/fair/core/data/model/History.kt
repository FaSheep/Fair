package org.fasheep.fair.core.data.model

import org.fasheep.fair.core.model.data.HistoryItem
import org.fasheep.fair.core.network.model.Assignment
import org.fasheep.fair.core.network.model.Num
import org.fasheep.fair.core.network.model.Vote


internal fun Assignment.toItem(): HistoryItem.Assignment {
    return HistoryItem.Assignment(timestamp, hash, name, role)
}

internal fun Num.toItem(): HistoryItem.Num {
    return HistoryItem.Num(timestamp, hash, value)
}

internal fun Vote.toItem(): HistoryItem.Vote {
    return HistoryItem.Vote(timestamp, hash, voteId, endTime, options)
}
