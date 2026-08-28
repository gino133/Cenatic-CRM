package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CustomerTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerTypeDao {
    @Query("SELECT * FROM customer_types ORDER BY sortOrder ASC, id ASC")
    fun getAllCustomerTypes(): Flow<List<CustomerTypeEntity>>

    @Query("SELECT * FROM customer_types ORDER BY sortOrder ASC, id ASC")
    suspend fun getAllCustomerTypesDirect(): List<CustomerTypeEntity>

    @Query("SELECT * FROM customer_types WHERE code = :code LIMIT 1")
    suspend fun getCustomerTypeByCode(code: String): CustomerTypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerType(type: CustomerTypeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerTypes(types: List<CustomerTypeEntity>): List<Long>

    @Update
    suspend fun updateCustomerType(type: CustomerTypeEntity)

    @Delete
    suspend fun deleteCustomerType(type: CustomerTypeEntity)

    @Query("DELETE FROM customer_types WHERE id = :id")
    suspend fun deleteCustomerTypeById(id: Long)

    @Query("DELETE FROM customer_types")
    suspend fun deleteAllCustomerTypes()

    @Query("SELECT COUNT(*) FROM customer_types")
    suspend fun getCount(): Int
}
