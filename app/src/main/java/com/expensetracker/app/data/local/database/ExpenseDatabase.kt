package com.expensetracker.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.expensetracker.app.data.local.dao.TransactionDao
import com.expensetracker.app.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        const val DATABASE_NAME = "expense_tracker.db"
    }
}
