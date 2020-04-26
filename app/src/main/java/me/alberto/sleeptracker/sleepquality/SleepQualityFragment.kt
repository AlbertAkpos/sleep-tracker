package me.alberto.sleeptracker.sleepquality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import me.alberto.sleeptracker.R
import me.alberto.sleeptracker.database.SleepDataBase
import me.alberto.sleeptracker.databinding.FragmentSleepQualityBinding

/**
 * A simple [Fragment] subclass.
 */
class SleepQualityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSleepQualityBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_sleep_quality,
                container, false
            )

        val application = requireNotNull(this.activity).application
        val arguments = SleepQualityFragmentArgs.fromBundle(requireArguments())
        val dataSource = SleepDataBase.getInstance(application).sleepDatabaseDao

        val sleepQualityViewModelFactory =
            SleepQualityViewModelFactory(arguments.sleepNightKey, dataSource)

        val sleepQualityViewModel = ViewModelProvider(
            this,
            sleepQualityViewModelFactory
        ).get(SleepQualityViewModel::class.java)

        binding.sleepQualityViewModel = sleepQualityViewModel

        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    this.findNavController().navigate(SleepQualityFragmentDirections
                        .actionSleepQualityFragmentToSleepTrackerFragment())

                    sleepQualityViewModel.doneNavigating()
                }
            }
        })

        return binding.root
    }

}
