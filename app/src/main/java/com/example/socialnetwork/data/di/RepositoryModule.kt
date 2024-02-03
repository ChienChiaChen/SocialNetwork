package com.example.socialnetwork.data.di

import com.example.socialnetwork.data.repository.AccountRepositoryImpl
import com.example.socialnetwork.data.repository.PostRepositoryImpl
import com.example.socialnetwork.domain.repository.AccountRepository
import com.example.socialnetwork.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideAccountRepository(
        repository: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    abstract fun providePostRepository(
        repository: PostRepositoryImpl
    ): PostRepository
}
