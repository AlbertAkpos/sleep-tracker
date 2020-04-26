package me.alberto.sleeptracker.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sleep_item_layout.view.*
import me.alberto.sleeptracker.R
import me.alberto.sleeptracker.convertDurationToFormatted
import me.alberto.sleeptracker.convertNumericQualityToString
import me.alberto.sleeptracker.database.SleepNight

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


    class TextItemViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
       private val sleepLength: TextView = view.findViewById<TextView>(R.id.sleep_length)
       private val quality: TextView = view.findViewById(R.id.quality_string)
       private val qualityImage: ImageView = view.quality_image


        fun bind(item: SleepNight) {
            val res = itemView.resources
            sleepLength.text = convertDurationToFormatted(
                item.startTimeMilli,
                item.endTimeMilli, res)

            quality.text = convertNumericQualityToString(item.sleepQuality, res)

            qualityImage.setImageResource(
                when (item.sleepQuality) {
                    0 -> R.drawable.ic_sleep_0
                    1 -> R.drawable.ic_sleep_1
                    2 -> R.drawable.ic_sleep_2
                    3 -> R.drawable.ic_sleep_3
                    4 -> R.drawable.ic_sleep_4
                    5 -> R.drawable.ic_sleep_5
                    else -> R.drawable.ic_sleep_active
                }
            )
        }


        companion object {
            fun from(parent: ViewGroup): TextItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.sleep_item_layout, parent, false)

                return TextItemViewHolder(view)
            }
        }

    }


}