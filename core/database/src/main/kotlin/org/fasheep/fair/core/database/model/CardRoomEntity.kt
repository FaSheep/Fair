package org.fasheep.fair.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.fasheep.fair.core.model.data.CardRoom

@Entity(tableName = "card_rooms")
data class CardRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "icon_path")
    val iconPath: String
)

fun CardRoomEntity.asExternalModel() = CardRoom(
    id = id,
    name = name,
    iconPath = iconPath
)
