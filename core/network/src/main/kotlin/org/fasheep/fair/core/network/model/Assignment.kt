package org.fasheep.fair.core.network.model

import com.apollographql.apollo.mpp.currentTimeMillis

data class Assignment internal constructor(
    val timestamp: Long = currentTimeMillis(),
    val hash: String,
    val name: List<String>,
    val role: List<String>
) {
    internal constructor(
        timestamp: String,
        hash: String,
        name: List<String>,
        role: List<String>
    ) : this(timestamp.toLong() * 1000, hash, name, role)
}
