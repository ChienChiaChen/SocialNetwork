package com.example.socialnetwork.network.di

import com.example.socialnetwork.data.connectivity.ConnectivityChecker
import com.example.socialnetwork.network.connectivity.ConnectivityCheckerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {

    @Binds
    @Singleton
    abstract fun bindConnectivityChecker(
        connectivityChecker: ConnectivityCheckerImpl
    ): ConnectivityChecker
}
