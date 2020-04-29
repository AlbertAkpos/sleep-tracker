package me.alberto.sleeptracker.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import me.alberto.sleeptracker.database.SleepDatabaseDao
import me.alberto.sleeptracker.database.SleepNight

class SleepDetailViewModel(private val sleepNightKey: Long = 0L, dataSource: SleepDatabaseDao) :
    ViewModel() {
    val database = dataSource

    private val viewModelJob = Job()
    private val night: LiveData<SleepNight>

    fun getNight() = night

    init {
        night = database.getNightWithId(sleepNightKey)
    }


    private val _navigateToSleepTracker = MutableLiveData<Boolean>()
    val navigateToSleepTracker: LiveData<Boolean>
        get() = _navigateToSleepTracker

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    fun onClose() {
        _navigateToSleepTracker.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}