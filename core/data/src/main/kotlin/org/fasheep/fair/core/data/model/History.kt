package org.fasheep.fair.core.data.model

import org.fasheep.fair.core.model.data.HistoryItem
import org.fasheep.fair.core.network.model.Assignment
import org.fasheep.fair.core.network.model.Num


internal fun Assignment.toItem(): HistoryItem.Assignment {
    return HistoryItem.Assignment(timestamp, name, role)
}

internal fun Num.toItem(): HistoryItem.Num {
    return HistoryItem.Num(timestamp, value)
}
