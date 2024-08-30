package org.fasheep.fair.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.fasheep.fair.core.database.model.CardRoomEntity
import org.fasheep.fair.core.database.model.RoleEntity

@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(roleEntity: RoleEntity)

    @Update
    suspend fun update(roleEntity: RoleEntity)

    @Delete
    suspend fun delete(roleEntity: RoleEntity)

    @Query("SELECT * from roles WHERE id = :id")
    fun getRole(id: Int): Flow<RoleEntity>

    @Query("SELECT * from roles")
    fun getRoles(): Flow<List<RoleEntity>>
}