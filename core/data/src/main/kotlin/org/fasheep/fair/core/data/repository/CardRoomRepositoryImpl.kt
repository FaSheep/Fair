package org.fasheep.fair.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.fasheep.fair.core.model.data.CardRoom
import org.fasheep.fair.core.model.data.Role
import org.fasheep.fair.core.database.dao.CardRoomDao
import org.fasheep.fair.core.database.dao.RoleDao
import org.fasheep.fair.core.database.dao.RoomDetailDao
import org.fasheep.fair.core.database.model.CardRoomEntity
import org.fasheep.fair.core.database.model.RoleEntity
import org.fasheep.fair.core.database.model.asExternalModel
import javax.inject.Inject

private const val TAG = "CardRoomRepository"

class CardRoomRepositoryImpl @Inject constructor(
    private val cardRoomDao: CardRoomDao,
    private val roomDetailDao: RoomDetailDao,
    private val roleDao: RoleDao
) : CardRoomRepository {
    override suspend fun add(cardRoom: CardRoom) {
        cardRoomDao.insert(
            CardRoomEntity(
                name = cardRoom.name,
                iconPath = cardRoom.iconPath
            )
        )
    }

    override suspend fun add(cardRoom: CardRoom, role: Role) {
        val id = cardRoomDao.insert(
            CardRoomEntity(
                name = cardRoom.name,
                iconPath = cardRoom.iconPath
            )
        )
        roleDao.insert(RoleEntity(roomId = id.toInt()))
    }

    override suspend fun remove(cardRoom: CardRoom) {
        cardRoomDao.delete(
            CardRoomEntity(
                id = cardRoom.id,
                name = cardRoom.name,
                iconPath = cardRoom.iconPath
            )
        )
    }

    override fun findById(id: Int): Flow<CardRoom> {
        return cardRoomDao.getCardRoom(id).map { it.asExternalModel() }
    }

    override fun findAll(): Flow<List<CardRoom>> {
        return cardRoomDao.getCardRooms().map { cardRooms ->
            cardRooms.map { it.asExternalModel() }
        }
    }

}