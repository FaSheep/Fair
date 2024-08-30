package org.fasheep.fair.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.fasheep.fair.core.data.repository.CardRoomRepository
import org.fasheep.fair.core.data.repository.CardRoomRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsRoomRepository(
        cardRoomRepositoryImpl: CardRoomRepositoryImpl
    ): CardRoomRepository
}