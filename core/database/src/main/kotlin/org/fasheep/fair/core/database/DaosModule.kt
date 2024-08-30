package org.fasheep.fair.core.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.fasheep.fair.core.database.dao.CardRoomDao
import org.fasheep.fair.core.database.dao.RoleDao
import org.fasheep.fair.core.database.dao.RoomDetailDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesCardRoomDao(
        database: FairDatabase,
    ): CardRoomDao = database.cardRoomDao()

    @Provides
    fun providesRoomDetailDao(
        database: FairDatabase,
    ): RoomDetailDao = database.roomDetailDao()

    @Provides
    fun providesRoleDao(
        database: FairDatabase,
    ): RoleDao = database.roleDao()
}
