/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepNightAdapter(val clickListener: SleepNightListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {
    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ITEM = 1

    // submitList()와 같은 리스트 조작 시 시간이 많이 걸릴 수 있으므로 (데이터가 많다면) 코루틴으로 조작한다.
    // 코루틴으로 해결?

    //Dispatchers.Default :
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
        }
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(getItem(position)!!, clickListener)
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("unknown viewType ${viewType}")
        }
    }

    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        // SleepNight data에 head data 추가하기
        // list 가 널이면 헤더만 추가 , 널이 아니면 헤더 + 리스트 데이터

        // 데이터가 엄청 많은 경우를 대비해 coroutine으로 돌리고 , UI변경은 메인 스레드로 스위칭
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            // 새 list 전송
            withContext(Dispatchers.Main) { // 스레드 메인으로 변경
                submitList(items)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}


class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

//    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
//        return oldItem.nightId == newItem.nightId
//    }
//
//    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
//        return oldItem == newItem
//    }
}


class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

sealed class DataItem {
    // data가 변경되었는지 확인하기 위해 DiffItemCallback이 각 item에 대한 id를 알기를 원한다.
    abstract val id: Long

    // 2개의 클래스 정의 1) sleepNight , 2) header
    data class SleepNightItem(val sleepNight: SleepNight): DataItem() {
        override val id: Long = sleepNight.nightId
    }

    // 실제 데이터가 없기 때문에 object 로 정의, 단지 하나의 인스턴스만 존재함을 의미한다.
    object Header: DataItem() {
        // 매우 작은 단위 이므로 어떤 id값과도 겹치지 않음
        override val id: Long = Long.MIN_VALUE
    }
}