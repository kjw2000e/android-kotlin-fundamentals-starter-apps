package com.example.android.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertLongToDateString
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.android.synthetic.main.custom_item_view.view.*

class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ItemViewHolder>() {
    var data = listOf<SleepNight>()
        // *setter 하는 부분 (데이터 변경시 set함수로 변경)
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent) // viewholder 인스턴스가 아닌 viewHolder class로 호출 (정적)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    class ItemViewHolder private constructor(view: View): RecyclerView.ViewHolder(view) {
        // private constructor : 생성자를 비공개 한다.
        // from 메소드가 ViewHolder 인스턴스를 반환하기 때문에 더 이상 뷰홀더의 생성자를 호출할 필요가 없다.
        // 따라서 비공개하기 위해 private constructor을 사용한다.
        private val img: ImageView = view.findViewById(R.id.iv_sleep)
        private val length: TextView = view.findViewById(R.id.tv_sleep_length)
        private val quality: TextView = view.findViewById(R.id.tv_quality)

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.custom_item_view, parent, false)
                return ItemViewHolder(view);
            }
        }

        fun bind(item: SleepNight) {
            val res = itemView.context.resources
            quality.text = convertNumericQualityToString(item.sleepQuality, res)
            length.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            img.setBackgroundResource(
                when (item.sleepQuality) {
                    0 -> R.drawable.ic_sleep_0
                    1 -> R.drawable.ic_sleep_1
                    2 -> R.drawable.ic_sleep_2
                    3 -> R.drawable.ic_sleep_3
                    4 -> R.drawable.ic_sleep_4
                    5 -> R.drawable.ic_sleep_5
                    else -> R.drawable.ic_sleep_0
                }
            )
        }
    }
}