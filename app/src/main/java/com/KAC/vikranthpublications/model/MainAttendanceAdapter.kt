package com.KAC.vikranthpublications.model

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.KAC.vikranthpublications.R

class MainAttendanceAdapter(private var monthList: ArrayList<MonthDataClass>):
    RecyclerView.Adapter<MainAttendanceAdapter.MonthViewHolder>() {


    inner class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthYearTextView: TextView = itemView.findViewById(R.id.attendancedateTextView)
        //val recyclerView: RecyclerView = itemView.findViewById(R.id.attendencedatesRecyclerView)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.attendance_recycler_layout, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthData = monthList[position]
        holder.monthYearTextView.text = monthData.month_year

        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return monthList.size
    }

    fun updateData(newMonthList: List<MonthDataClass>) {
        monthList.clear()
        monthList.addAll(newMonthList)
        notifyDataSetChanged()
    }

}