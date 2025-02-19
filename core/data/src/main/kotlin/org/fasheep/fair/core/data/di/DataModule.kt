package org.fasheep.fair.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.fasheep.fair.core.data.repository.DefaultHistoryRepository
import org.fasheep.fair.core.data.repository.HistoryRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsHistoryRepository(
        defaultHistoryRepository: DefaultHistoryRepository
    ): HistoryRepository
}
