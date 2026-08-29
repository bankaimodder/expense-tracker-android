package com.expensetracker.app.di

import android.content.Context
import androidx.room.Room
import com.expensetracker.app.data.local.dao.TransactionDao
import com.expensetracker.app.data.local.database.ExpenseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            ExpenseDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideTransactionDao(database: ExpenseDatabase): TransactionDao {
        return database.transactionDao()
    }
}
