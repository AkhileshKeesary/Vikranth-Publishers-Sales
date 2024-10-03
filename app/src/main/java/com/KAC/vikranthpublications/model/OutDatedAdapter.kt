package com.KAC.vikranthpublications.model

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.KAC.vikranthpublications.R


class OutDatedAdapter(private  var outdatedtasklist: ArrayList<OutdatedDataClass>):
    RecyclerView.Adapter<OutDatedAdapter.myViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.outdated_leads_layout,parent,false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentitem = outdatedtasklist[position]
        holder.schoolname.setTextColor(Color.RED)
        holder.schoolname.text = currentitem.school_name
        holder.followupdate.text = currentitem.follow_up_date
        holder.outdated.setTextColor(Color.RED)
        holder.outdated.text = "OUTDATED"



    }

    override fun getItemCount(): Int {
        return outdatedtasklist.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun searchDataList(searchList: ArrayList<OutdatedDataClass>) {
        outdatedtasklist = searchList
        notifyDataSetChanged()
    }

    class myViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val schoolname : TextView = itemView.findViewById(R.id.reminderschollnameTxt)
        val followupdate : TextView = itemView.findViewById(R.id.reminderschoolfollowupdate)
        val outdated : TextView = itemView.findViewById(R.id.timerTxt)



    }
    fun clearData() {
        outdatedtasklist.clear()
        notifyDataSetChanged()
    }
    fun updateData(newData: List<OutdatedDataClass>) {
        outdatedtasklist.clear()
        outdatedtasklist.addAll(newData)
        notifyDataSetChanged()
    }
}