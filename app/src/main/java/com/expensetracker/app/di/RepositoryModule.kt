package com.expensetracker.app.di

import com.expensetracker.app.data.repository.SettingsRepositoryImpl
import com.expensetracker.app.data.repository.TransactionRepositoryImpl
import com.expensetracker.app.domain.repository.SettingsRepository
import com.expensetracker.app.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}
