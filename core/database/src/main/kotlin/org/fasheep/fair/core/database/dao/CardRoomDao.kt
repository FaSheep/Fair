package org.fasheep.fair.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.fasheep.fair.core.database.model.CardRoomEntity

@Dao
interface CardRoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cardRoomEntity: CardRoomEntity): Long

    @Update
    suspend fun update(cardRoomEntity: CardRoomEntity)

    @Delete
    suspend fun delete(cardRoomEntity: CardRoomEntity)

    @Query("SELECT * from card_rooms WHERE id = :id")
    fun getCardRoom(id: Int): Flow<CardRoomEntity>

    @Query("SELECT * from card_rooms ORDER BY name ASC")
    fun getCardRooms(): Flow<List<CardRoomEntity>>
}