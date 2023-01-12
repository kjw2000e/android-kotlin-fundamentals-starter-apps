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
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleeNightDiffCallback()) {
//    var data =  listOf<SleepNight>()
//        set(value) {
//            field = value
//            notifyDataSetChanged() // rebind , redraw all list
//        }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = data[position]
        val item = getItem(position)
        holder.bind(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root){
        val sleepLength: TextView = binding.sleepLength
        val quality: TextView = binding.qualityString
        val qualityImage: ImageView = binding.qualityImage

        fun bind(item: SleepNight) {
            binding.sleep = item
            binding.executePendingBindings()
            // executePendingBindings() : 보류 중인 바인딩을 즉시 실행하도록 데이터 바인딩을 요청하는 최적화다.
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                // 차이점이 뭔가
//                val binding2 : ListItemSleepNightBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_sleep_night, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class SleeNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            // 아이템이 같은가 (지정한 필드의 내용이 같은가 확인)
            return oldItem.nightId == newItem.nightId
        }

        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            // 동일한 데이터를 포함하는지 확인, 모든 필드 검사
            return oldItem == newItem
        }
    }
}