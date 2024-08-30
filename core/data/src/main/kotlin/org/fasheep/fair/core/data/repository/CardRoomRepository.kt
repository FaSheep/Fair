package org.fasheep.fair.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.fasheep.fair.core.model.data.CardRoom
import org.fasheep.fair.core.model.data.Role

interface CardRoomRepository {
    suspend fun add(cardRoom: CardRoom)

    suspend fun remove(cardRoom: CardRoom)

    suspend fun add(cardRoom: CardRoom, role: Role)

    fun findById(id: Int): Flow<CardRoom>

    fun findAll(): Flow<List<CardRoom>>
}