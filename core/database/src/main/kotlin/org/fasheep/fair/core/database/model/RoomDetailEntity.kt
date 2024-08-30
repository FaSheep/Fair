package org.fasheep.fair.core.database.model

import androidx.room.Embedded
import androidx.room.Relation


data class RoomDetailEntity(
    @Embedded
    val cardRoomEntity: CardRoomEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "room_id"
    )
    val roleEntity: List<RoleEntity>
)
