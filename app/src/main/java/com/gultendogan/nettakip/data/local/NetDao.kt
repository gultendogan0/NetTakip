package com.gultendogan.nettakip.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import java.util.*

@Dao
interface NetDao {
    @Query("SELECT * FROM net ORDER BY timestamp DESC LIMIT 1")
    fun fetchLastNet() :List<NetEntity>

    @Query("SELECT * FROM net WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<NetEntity>

    @Query("SELECT * FROM net WHERE timestamp BETWEEN :startDate AND :endDate")
    fun fetchBy(startDate:Date, endDate:Date) : List<NetEntity>

    @Insert
    fun insertAll(vararg users: NetEntity)

    @Insert
    fun save(net: NetEntity)

    @Update
    suspend fun update(net: NetEntity)

    @Delete
    suspend fun delete(net: NetEntity)

    @Query("SELECT * FROM net ORDER BY timestamp DESC")
    fun getAllNets(): Flow<List<NetEntity>>

    @Query("SELECT * FROM net ORDER BY timestamp DESC LIMIT 1")
    fun getLast5Nets():Flow<List<NetEntity>>

    @Query("SELECT AVG(value) as average FROM net WHERE timestamp BETWEEN :startDay AND :endDay ORDER BY timestamp ASC")
    fun getAverageByDateRange(
        startDay:Date,
        endDay:Date
    ): Float?

    @Query("SELECT AVG(value) as average FROM net ORDER BY timestamp ASC")
    fun getAvg(): Flow<Float?>

    @Query("SELECT MAX(value) FROM net ORDER BY timestamp ASC")
    fun getMax(): Flow<Float?>

    @Query("SELECT MIN(value)  FROM net ORDER BY timestamp ASC")
    fun getMin(): Flow<Float?>
}