package org.fasheep.fair.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.fasheep.fair.core.database.dao.CardRoomDao
import org.fasheep.fair.core.database.dao.RoleDao
import org.fasheep.fair.core.database.dao.RoomDetailDao
import org.fasheep.fair.core.database.model.CardRoomEntity
import org.fasheep.fair.core.database.model.RoleEntity

@Database(
    entities = [CardRoomEntity::class, RoleEntity::class],
    version = 2,
    exportSchema = false
)
abstract class FairDatabase : RoomDatabase() {

    abstract fun cardRoomDao(): CardRoomDao

    abstract fun roleDao(): RoleDao

    abstract fun roomDetailDao(): RoomDetailDao

//    companion object {
//        @Volatile
//        private var INSTANCE: FairDatabase? = null
//
//        fun getDatabase(context: Context): FairDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    FairDatabase::class.java,
//                    "fair_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
}
