package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DealDao {
    @Query("SELECT * FROM deals ORDER BY createdAt DESC")
    fun getAllDeals(): Flow<List<DealEntity>>

    @Query("SELECT * FROM deals WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getDealsByCustomer(customerId: Long): Flow<List<DealEntity>>

    @Query("SELECT * FROM deals WHERE stage = :stage ORDER BY createdAt DESC")
    fun getDealsByStage(stage: String): Flow<List<DealEntity>>

    @Query("SELECT * FROM deals WHERE id = :id")
    fun getDealById(id: Long): Flow<DealEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeal(deal: DealEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeals(deals: List<DealEntity>): List<Long>

    @Update
    suspend fun updateDeal(deal: DealEntity)

    @Query("UPDATE deals SET stage = :newStage WHERE id = :dealId")
    suspend fun updateDealStage(dealId: Long, newStage: String)

    @Delete
    suspend fun deleteDeal(deal: DealEntity)

    @Query("DELETE FROM deals WHERE id = :id")
    suspend fun deleteDealById(id: Long)

    @Query("SELECT SUM(value) FROM deals WHERE stage = 'WON'")
    fun getTotalWonRevenue(): Flow<Double?>

    @Query("SELECT SUM(value) FROM deals WHERE stage != 'LOST'")
    fun getTotalPipelineValue(): Flow<Double?>
}
