package me.alberto.sleeptracker.sleepdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController

import me.alberto.sleeptracker.R
import me.alberto.sleeptracker.database.SleepDataBase
import me.alberto.sleeptracker.databinding.FragmentSleepDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class SleepDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSleepDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sleep_detail,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = SleepDataBase.getInstance(application).sleepDatabaseDao
        val arguments = SleepDetailFragmentArgs.fromBundle(requireArguments())

        val sleepDetailViewModelFactory = SleepDetailViewModelFactory(arguments.sleepNightKey, dataSource)
        val sleepDetailViewModel = ViewModelProvider(this, sleepDetailViewModelFactory).get(SleepDetailViewModel::class.java)

        binding.sleepDetailViewModel = sleepDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        sleepDetailViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            if (it == true){
                this.findNavController().navigate(
                    SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment()
                )
                sleepDetailViewModel.doneNavigating()
            }
        })



        return binding.root
    }

}
