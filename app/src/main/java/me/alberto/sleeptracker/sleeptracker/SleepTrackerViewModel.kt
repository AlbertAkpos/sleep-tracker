package me.alberto.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import me.alberto.sleeptracker.database.SleepDatabaseDao

class SleepTrackerViewModel(val database: SleepDatabaseDao,
                            application: Application): AndroidViewModel(application){

}