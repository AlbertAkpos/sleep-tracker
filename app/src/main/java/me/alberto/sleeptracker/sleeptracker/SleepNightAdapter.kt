package me.alberto.sleeptracker.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.alberto.sleeptracker.R
import me.alberto.sleeptracker.database.SleepNight
import me.alberto.sleeptracker.databinding.SleepItemLayoutBinding
import java.lang.ClassCastException

private const val HEADER_VIEW_TYPE = 0
private const val SLEEP_ITEM_VIEW_TYPE = 1

class SleepNightAdapter(private val clickListner: SleepNightListener) : ListAdapter<SleepNightAdapter.DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

        private val adapterScope = CoroutineScope(Dispatchers.Default)

    class SleepNightDiffCallback: DiffUtil.ItemCallback<DataItem>(){
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SLEEP_ITEM_VIEW_TYPE -> SleepItemViewHolder.from(parent)
            HEADER_VIEW_TYPE -> HeaderTextViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    fun addHeaderAndSubmitList(list: List<SleepNight>){
        adapterScope.launch {
            val items = when(list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }

            withContext(Dispatchers.Main) {
                submitList(items)
            }

        }
    }


    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.Header -> HEADER_VIEW_TYPE
            is DataItem.SleepNightItem -> SLEEP_ITEM_VIEW_TYPE
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is SleepItemViewHolder -> {
                val item = getItem(position) as DataItem.SleepNightItem
                holder.bind(item.sleepNight, clickListner)
            }
        }

    }

    class HeaderTextViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): HeaderTextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return HeaderTextViewHolder(view)
            }
        }
    }

    class SleepItemViewHolder private constructor(private val binding: SleepItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: SleepNight,
            clickListener: SleepNightListener
        ) {
            binding.sleepNight = item
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): SleepItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SleepItemLayoutBinding.inflate(layoutInflater, parent, false)
                return SleepItemViewHolder(binding)
            }
        }

    }

    class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
        fun onClick(night: SleepNight) = clickListener(night.nightId)
    }


    sealed class DataItem {
        abstract val id: Long
        data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
            override val id = sleepNight.nightId
        }
        object Header : DataItem() {
            override val id: Long
                get() = Long.MIN_VALUE
        }
    }

}