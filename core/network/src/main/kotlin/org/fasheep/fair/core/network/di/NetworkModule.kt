package org.fasheep.fair.core.network.di

import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @RandomClient
    @Provides
    fun provideRandomClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://api.studio.thegraph.com/query/99199/random/version/latest")
            .build()
    }

    @AssignClient
    @Provides
    fun provideAssignClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://api.studio.thegraph.com/query/99199/assign/version/latest")
            .build()
    }

    @VoteClient
    @Provides
    fun provideVoteClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://api.studio.thegraph.com/query/99199/vote/version/latest")
            .build()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RandomClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AssignClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VoteClient
