package me.alberto.sleeptracker.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.alberto.sleeptracker.R
import me.alberto.sleeptracker.convertDurationToFormatted
import me.alberto.sleeptracker.convertNumericQualityToString
import me.alberto.sleeptracker.database.SleepNight
import me.alberto.sleeptracker.databinding.SleepItemLayoutBinding

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.TextItemViewHolder>(SleepNightDiffCallback()) {

    class SleepNightDiffCallback: DiffUtil.ItemCallback<SleepNight>(){
        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem.nightId == newItem.nightId
        }

        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        return TextItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class TextItemViewHolder private constructor(private val binding: SleepItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: SleepNight) {
            binding.sleepNight = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TextItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SleepItemLayoutBinding.inflate(layoutInflater, parent, false)
                return TextItemViewHolder(binding)
            }
        }

    }


}