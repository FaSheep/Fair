package org.fasheep.fair.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.fasheep.fair.core.database.model.RoomDetailEntity

@Dao
interface RoomDetailDao {
    @Transaction
    @Query("SELECT * from card_rooms WHERE id = :id")
    fun getRoomDetail(id: Int): Flow<RoomDetailEntity>

    @Transaction
    @Query("SELECT * from card_rooms ORDER BY name ASC")
    fun getRoomDetails(): Flow<List<RoomDetailEntity>>
}