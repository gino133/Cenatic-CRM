package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CustomerEntity
import com.example.data.model.CustomerTypeEntity
import com.example.data.model.DealEntity
import com.example.data.model.InteractionEntity
import com.example.data.model.TaskEntity

@Database(
    entities = [
        CustomerEntity::class,
        DealEntity::class,
        InteractionEntity::class,
        TaskEntity::class,
        CustomerTypeEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun customerTypeDao(): CustomerTypeDao
    abstract fun dealDao(): DealDao
    abstract fun interactionDao(): InteractionDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "crm_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
