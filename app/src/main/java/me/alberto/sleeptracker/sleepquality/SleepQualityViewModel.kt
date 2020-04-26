package me.alberto.sleeptracker.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import me.alberto.sleeptracker.database.SleepDatabaseDao

class SleepQualityViewModel (
    private val sleepNightKey: Long = 0L,
    val database: SleepDatabaseDao): ViewModel(){

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker:LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    fun onSetSleepQuality(sleepQuality: Int) {
        uiScope.launch {
            updateSleepNight(sleepQuality)
            _navigateToSleepTracker.value = true
        }
    }

    private suspend fun updateSleepNight(sleepQuality: Int) {
        withContext(Dispatchers.IO){
            val tonight = database.get(sleepNightKey) ?: return@withContext
            tonight.sleepQuality = sleepQuality
            database.update(tonight)
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}