package me.alberto.sleeptracker.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import me.alberto.sleeptracker.R
import me.alberto.sleeptracker.database.SleepDataBase
import me.alberto.sleeptracker.databinding.FragmentSleepTrackerBinding

/**
 * A simple [Fragment] subclass.
 */
class SleepTrackerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sleep_tracker,
            container,
            false
        )


        val application = requireNotNull(this.activity).application
        val dataSource = SleepDataBase.getInstance(application)
            .sleepDatabaseDao

        val sleepTrackerViewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        val sleepTrackerViewModel = ViewModelProvider(
            this,
            sleepTrackerViewModelFactory
        ).get(SleepTrackerViewModel::class.java)

        val manager = GridLayoutManager(activity, 3)

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return when(position){
                    0 -> 3
                    else -> 1
                }
            }
        }

        binding.sleepTrackerViewModel = sleepTrackerViewModel
        binding.lifecycleOwner = this
        binding.sleepList.layoutManager = manager

        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->

            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId)
                )

                sleepTrackerViewModel.doneNavigating()
            }

        })

        sleepTrackerViewModel.navigateToSleepDetail.observe(viewLifecycleOwner, Observer { nightId ->
            nightId?.let {
                this.findNavController().navigate(SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepDetailFragment(nightId))
                sleepTrackerViewModel.onSleepDetailNavigated()
            }

        })

        sleepTrackerViewModel.showSnackbarEvent.observe(viewLifecycleOwner, Observer {
            if (it){
                Snackbar.make(requireActivity().findViewById(R.id.start_button), getString(R.string.cleared_message), Snackbar.LENGTH_SHORT).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })

        val adapter = SleepNightAdapter(SleepNightAdapter.SleepNightListener { sleepNightKey ->
            sleepTrackerViewModel.onSleepNightClicked(sleepNightKey)
        })

        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.addHeaderAndSubmitList(it)}
        })

        binding.sleepList.adapter = adapter
        return binding.root
    }

}
