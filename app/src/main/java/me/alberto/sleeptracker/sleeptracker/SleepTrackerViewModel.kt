package me.alberto.sleeptracker.sleeptracker

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import me.alberto.sleeptracker.database.SleepDatabaseDao
import me.alberto.sleeptracker.database.SleepNight
import me.alberto.sleeptracker.formatNights

class SleepTrackerViewModel(val database: SleepDatabaseDao,
                            application: Application): AndroidViewModel(application){

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val tonight = MutableLiveData<SleepNight?>()

    val nights = database.getAllNights()


    val startButtonVisible: LiveData<Boolean> = Transformations.map(tonight){ it == null}

    val stopButtonVisible: LiveData<Boolean> = Transformations.map(tonight) { it != null }

    val clearButtonVisible: LiveData<Boolean> = Transformations.map(nights){ it.isNotEmpty() }


    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackbarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    private var _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    private val _navigateToSleepDetail = MutableLiveData<Long>()
    val navigateToSleepDetail: LiveData<Long>
        get() = _navigateToSleepDetail

    fun onSleepDetailNavigated() {
        _navigateToSleepDetail.value = null
    }

    //Format nights
    val nightsString: LiveData<Spanned> = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
       uiScope.launch {
           tonight.value = getTonightFromDatabase()
       }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO){
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli){
                night = null
            }
            night
        }
    }


    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    fun onStopTracking() {
        uiScope.launch{
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(oldNight: SleepNight){
        withContext(Dispatchers.IO){
            database.update(oldNight)
        }
    }

    private suspend fun insert(night: SleepNight){
        withContext(Dispatchers.IO){
            database.insert(night)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
            _showSnackbarEvent.value = true
        }
    }

    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }

    fun onSleepNightClicked(id: Long) {
        _navigateToSleepDetail.value = id
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}