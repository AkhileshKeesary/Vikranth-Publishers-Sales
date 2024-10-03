package com.KAC.vikranthpublications.model

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.KAC.vikranthpublications.R

class  CompletedAdapter(private  var tasklist: ArrayList<CompletedLeadDataClass>): RecyclerView.Adapter<CompletedAdapter.myViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.completed_recycler_layout,parent,false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentitem = tasklist[position]
        holder.schoolname.text = currentitem.school_name
        holder.date.text = currentitem.task_closed_date
        holder.schoolphoneno.text = currentitem.school_phone_number
        //holder.schoolemail.text = currentitem.school_email
        //holder.schooladdress.text = currentitem.school_address
        holder.noofstudents.text = currentitem.no_of_students

        holder.expectedordervalue.text = currentitem.expected_order_value
        holder.expectedorderdate.text = currentitem.expected_order_date
        holder.finaloutcome.text = currentitem.final_outcome
        holder.finalordervalue.text = currentitem.final_order_value
        //holder.installment1.text = currentitem.installment_1
        // holder.installment2.text = currentitem.installment_2
        //holder.installment3.text = currentitem.installment_3
        holder.contacttname.text = currentitem.contact_person_name
        holder.contactphone.text = currentitem.contact_person_phone
        //holder.contactemail.text = currentitem.contact_person_email
        holder.decisionmakername.text = currentitem.desicion_maker_name
        //holder.decisionmakeremail.text = currentitem.desicion_maker_email
        holder.decisionmakerphone.text = currentitem.desicion_maker_phone
        //holder.decisionmakerdesignation.text = currentitem.desicion_maker_designation
        //holder.feedback.text = currentitem.feedback
        if (currentitem.prospect.toString().length == 4) {
            holder.prospect.setTextColor(Color.RED)
            holder.prospect.text = currentitem.prospect
        }
        if (currentitem.prospect.toString().length == 3) {
            holder.prospect.setTextColor(Color.GREEN)
            holder.prospect.text = currentitem.prospect
        }
        if (currentitem.prospect.toString().length == 6) {
            holder.prospect.setTextColor(Color.MAGENTA)
            holder.prospect.text = currentitem.prospect
        }

        if (position == itemCount - 1) {
            val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = 240
            holder.itemView.layoutParams = params
        } else {
            val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = 30
            holder.itemView.layoutParams = params
        }



    }

    override fun getItemCount(): Int {
        return tasklist.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun searchDataList(searchList: ArrayList<CompletedLeadDataClass>) {
        tasklist = searchList
        notifyDataSetChanged()
    }

    class myViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val schoolname : TextView = itemView.findViewById(R.id.UschoolnameTxt)
        val date : TextView = itemView.findViewById(R.id.UcreateddateTxt)
        val schoolphoneno : TextView = itemView.findViewById(R.id.Uschoolphonenotxt)
        //val schoolemail : TextView = itemView.findViewById(R.id.UschoolemailTxt)
        //val schooladdress : TextView = itemView.findViewById(R.id.Uschooladdresstxt)
        val noofstudents : TextView = itemView.findViewById(R.id.UnoofstudentsTxt)
        val prospect : TextView = itemView.findViewById(R.id.UprospectTxt)
        val expectedordervalue : TextView = itemView.findViewById(R.id.UexpectedordervalueTxt)
        val expectedorderdate : TextView = itemView.findViewById(R.id.UexpectedorderdateTxt)
        val finaloutcome : TextView = itemView.findViewById(R.id.UfinaloutcomeTxt)
        val finalordervalue : TextView = itemView.findViewById(R.id.UfinalordervalueTxt)
        //val installment1 : TextView = itemView.findViewById(R.id.Uinstallment1Txt)
        //val installment2 : TextView = itemView.findViewById(R.id.Uinstallment2Txt)
        //val installment3 : TextView = itemView.findViewById(R.id.Uinstallment3Txt)
        val contacttname : TextView = itemView.findViewById(R.id.UcontactnameTxt)
        val contactphone : TextView = itemView.findViewById(R.id.UcontactphoneTxt)
        //val contactemail : TextView = itemView.findViewById(R.id.UcontatemailTxt)
        val decisionmakername : TextView = itemView.findViewById(R.id.UdesicionmakenameTxt)
        //val decisionmakeremail : TextView = itemView.findViewById(R.id.UdescisonmakeremailTxt)
        //val decisionmakerdesignation : TextView = itemView.findViewById(R.id.UdesicionmakedesignationTxt)
        val decisionmakerphone : TextView = itemView.findViewById(R.id.UdesicionmakephoneTxt)
        //val feedback : TextView = itemView.findViewById(R.id.UfeedbackTxt)

    }
    fun clearData() {
        tasklist.clear()
        notifyDataSetChanged()
    }
    fun updateData(newData: List<CompletedLeadDataClass>) {
        tasklist.clear()
        tasklist.addAll(newData)
        notifyDataSetChanged()
    }
}