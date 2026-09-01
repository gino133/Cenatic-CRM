package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.InteractionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InteractionDao {
    @Query("SELECT * FROM interactions ORDER BY date DESC")
    fun getAllInteractions(): Flow<List<InteractionEntity>>

    @Query("SELECT * FROM interactions WHERE customerId = :customerId ORDER BY date DESC")
    fun getInteractionsByCustomer(customerId: Long): Flow<List<InteractionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInteraction(interaction: InteractionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInteractions(interactions: List<InteractionEntity>): List<Long>

    @Update
    suspend fun updateInteraction(interaction: InteractionEntity)

    @Delete
    suspend fun deleteInteraction(interaction: InteractionEntity)

    @Query("DELETE FROM interactions WHERE id = :id")
    suspend fun deleteInteractionById(id: Long)

    @Query("DELETE FROM interactions")
    suspend fun deleteAllInteractions()

    @Query("SELECT * FROM interactions ORDER BY date DESC LIMIT :limit")
    fun getRecentInteractions(limit: Int = 10): Flow<List<InteractionEntity>>
}
