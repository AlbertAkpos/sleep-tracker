package me.alberto.sleeptracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao{
    @Insert
    fun insert(sleepNight: SleepNight)

    @Update
    fun update(sleepNight: SleepNight)

    @Query("SELECT * from daily_sleep_quality_table WHERE nightId =:key")
    fun get(key: Long): SleepNight?

    @Query("DELETE from daily_sleep_quality_table")
    fun clear()

    @Query("SELECT * from daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getTonight(): SleepNight?

    @Query("SELECT * from daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key ")
    fun getNightWithId(key: Long): LiveData<SleepNight>

}