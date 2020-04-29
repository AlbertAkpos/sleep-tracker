package me.alberto.sleeptracker.sleepdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.alberto.sleeptracker.database.SleepDatabaseDao
import java.lang.IllegalArgumentException

class SleepDetailViewModelFactory(
    private val sleepNightKey: Long=0L,
    private val dataSource: SleepDatabaseDao
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepDetailViewModel::class.java)){
            return SleepDetailViewModel(sleepNightKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}