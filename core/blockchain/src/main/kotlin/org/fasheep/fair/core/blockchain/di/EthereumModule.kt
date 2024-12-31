package org.fasheep.fair.core.blockchain.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.metamask.androidsdk.DappMetadata
import io.metamask.androidsdk.DefaultLogger
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.EthereumFlow
import io.metamask.androidsdk.EthereumFlowWrapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object EthereumModule {
    @Singleton
    @Provides
    fun provideEthereum(@ApplicationContext context: Context): Ethereum {
        return Ethereum(context, DappMetadata("Fair", "https://www.fasheep.xyz"), null, DefaultLogger)
    }

    @Singleton
    @Provides
    fun provideEthereumFlow(ethereum: Ethereum): EthereumFlow {
        return EthereumFlow(ethereum)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class WrapperModule {
    @Binds
    abstract fun bindEthereumWrapper(ethereumFlow: EthereumFlow): EthereumFlowWrapper
}