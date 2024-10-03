package com.KAC.vikranthpublications.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.KAC.vikranthpublications.R
import com.KAC.vikranthpublications.view.activities.FinalformActivity
import com.KAC.vikranthpublications.view.activities.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class UpcommingAdapter(private  var upcommingtasklist: ArrayList<NewLeadDataClass>): RecyclerView.Adapter<UpcommingAdapter.myViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.upcomming_recycler_layout,parent,false)
        return myViewHolder(itemView)
    }

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentitem = upcommingtasklist[position]
        val check = currentitem.status.toString().trim()
        val checkvalue = "Completed"
        holder.schoolname.text = currentitem.school_name
        //holder.schoolphoneno.text = currentitem.school_phone_number
        //holder.schoolemail.text = currentitem.school_email
        //holder.schooladdress.text = currentitem.school_address
        //holder.noofstudents.text = currentitem.no_of_students
        val followup = currentitem.follow_up_date
        holder.followupdate.text = "Follow up date : $followup"
        //holder.currentsupplier.text = currentitem.current_supplier
        //holder.currentsupplierdiscount.text = currentitem.current_supplier_discount_percentage
        //holder.contactpersonname.text = currentitem.contact_person_name
        //holder.contactpersonphone.text = currentitem.contact_person_phone
        //holder.contactpersonemail.text = currentitem.contact_person_email
       // holder.desicionmakername.text = currentitem.desicion_maker_name
        //holder.desicionmakerphone.text = currentitem.desicion_maker_phone
        //holder.desicionmakeremail.text = currentitem.desicion_maker_email
        //holder.desicionmakerdesignation.text = currentitem.desicion_maker_designation
        val generateddate = currentitem.task_generated_date
        holder.date.text = "Lead Generated Date : $generateddate"
        //holder.status.text = currentitem.status
        val prospect = currentitem.prospect
        if (currentitem.prospect!!.length == 4) {
            holder.prospect.setTextColor(Color.RED)
            holder.prospect.text = "Prospect : $prospect"
        }
        if (currentitem.prospect!!.length == 3) {
            holder.prospect.setTextColor(Color.GREEN)
            holder.prospect.text = "Prospect : $prospect"
        }
        if (currentitem.prospect!!.length == 6) {
            holder.prospect.setTextColor(Color.MAGENTA)
            holder.prospect.text = "Prospect : $prospect"
        }

        /*holder.itemView.setOnClickListener {
            if (check == checkvalue) {
                Toast.makeText(holder.itemView.context, "This item is already completed", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(holder.itemView.context, FinalformActivity::class.java)
                intent.putExtra("UniqueKey", currentitem.uniqueKey)
                intent.putExtra("TDate", currentitem.task_generated_date)
                intent.putExtra("TTime", currentitem.ttime)
                intent.putExtra("SchoolName", currentitem.school_name)
                intent.putExtra("SchoolEmail", currentitem.school_email)
                intent.putExtra("SchoolPhone", currentitem.school_phone_number)
                intent.putExtra("SchoolAddress", currentitem.school_address)
                intent.putExtra("SchoolPincode", currentitem.school_pincode)
                intent.putExtra("NoOfStudents", currentitem.no_of_students)
                intent.putExtra("ContactPersonName", currentitem.contact_person_name)
                intent.putExtra("ContactPersonPhone", currentitem.contact_person_phone)
                intent.putExtra("ContactPersonEmail", currentitem.contact_person_email)
                intent.putExtra("DesicionName", currentitem.desicion_maker_name)
                intent.putExtra("DesicionPhone", currentitem.desicion_maker_phone)
                intent.putExtra("DesicionEmail", currentitem.desicion_maker_email)
                intent.putExtra("DesicionDesignation", currentitem.desicion_maker_designation)
                intent.putExtra("CurrentSupplier", currentitem.current_supplier)
                intent.putExtra("CurrentSupplierDiscount", currentitem.current_supplier_discount_percentage)
                intent.putExtra("Status", currentitem.status)
                intent.putExtra("FollowUpDate", currentitem.follow_up_date)
                intent.putExtra("Prospect", currentitem.prospect)
                holder.itemView.context.startActivity(intent)
            }
        }
       // holder.moredetails.setOnClickListener {
            //val additionaldeatils : LinearLayout = holder.itemView.findViewById(R.id.addictiondeatilsLinearlayout)
            //additionaldeatils.visibility = View.VISIBLE
            //val moredetails : LinearLayout = holder.itemView.findViewById(R.id.moredetailslinearlayout)
            //moredetails.visibility = View.GONE
            //val emailid : LinearLayout = holder.itemView.findViewById(R.id.schoolemaillinearlayout)
           // val address : LinearLayout = holder.itemView.findViewById(R.id.schooladdresslinearlayout)
           // val nos : LinearLayout = holder.itemView.findViewById(R.id.noslinearlayout)
           // emailid.visibility = View.VISIBLE
           // address.visibility = View.VISIBLE
            //nos.visibility = View.VISIBLE
        //} */

        holder.viewdetailsButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(holder.itemView.context)
            val bottomSheetView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.view_details_bottom_layout, null)

            val schoolName: TextView = bottomSheetView.findViewById(R.id.UschoolnameTxt)
            val schoolPhone: TextView = bottomSheetView.findViewById(R.id.Uschoolphonenotxt)
            val schoolEmail: TextView = bottomSheetView.findViewById(R.id.Uschoolemailtxt)
            val schoolAddress: TextView = bottomSheetView.findViewById(R.id.Uschooladdresstxt)
            val studentsCount: TextView = bottomSheetView.findViewById(R.id.UnoofstudentsTxt)
            val contactName: TextView = bottomSheetView.findViewById(R.id.contactnameTxt)
            val contactPhone: TextView = bottomSheetView.findViewById(R.id.contactphoneTxt)
            val contactEmail: TextView = bottomSheetView.findViewById(R.id.contactemailTxt)
            val dMakerName: TextView = bottomSheetView.findViewById(R.id.desicionmakernameTxt)
            val dMakerPhone: TextView = bottomSheetView.findViewById(R.id.desicionmakerphonenoTxt)
            val dMakerEmail: TextView = bottomSheetView.findViewById(R.id.desicsionmakeremailTxt)
            val dMakerDesignation: TextView = bottomSheetView.findViewById(R.id.desicionmakerdesinationTxt)
            val currentSupplier: TextView = bottomSheetView.findViewById(R.id.currentsupplierTxt)
            val supplierDiscount: TextView = bottomSheetView.findViewById(R.id.currentsuplierdiscountTxt)
            val prospect: TextView = bottomSheetView.findViewById(R.id.UprospectTxt)
            val followUpDate: TextView = bottomSheetView.findViewById(R.id.UfollowupdateTxt)
            val date: TextView = bottomSheetView.findViewById(R.id.UcreateddateTxt)
            val Timer: TextView = bottomSheetView.findViewById(R.id.countdownTimerTextView)

            schoolName.text = currentitem.school_name
            schoolEmail.text = currentitem.school_email
            schoolAddress.text = currentitem.school_address
            contactName.text = currentitem.contact_person_name
            contactEmail.text = currentitem.contact_person_email
            contactPhone.text = currentitem.contact_person_phone
            schoolPhone.text = currentitem.school_phone_number
            studentsCount.text = currentitem.no_of_students
            val currentprospect = currentitem.prospect
            if (currentitem.prospect.length == 4) {
                prospect.setTextColor(Color.RED)
                prospect.text = "$currentprospect"
            }
            if (currentitem.prospect.length == 3) {
               prospect.setTextColor(Color.GREEN)
                prospect.text = "$currentprospect"
            }
            if (currentitem.prospect.length == 6) {
                prospect.setTextColor(Color.MAGENTA)
                prospect.text = "$currentprospect"
            }
            val generateddate = currentitem.task_generated_date
            date.text = "Lead Generated Date : $generateddate"
            dMakerName.text = currentitem.desicion_maker_name
            dMakerPhone.text = currentitem.desicion_maker_phone
            dMakerEmail.text = currentitem.desicion_maker_email
            dMakerDesignation.text = currentitem.desicion_maker_designation
            currentSupplier.text = currentitem.current_supplier
            supplierDiscount.text = currentitem.current_supplier_discount_percentage
            val followup = currentitem.follow_up_date
            followUpDate.text = "Follow up date : $followup"



            val enddate = currentitem.follow_up_timestamp!!.toString()
            val currentTimeMillis = System.currentTimeMillis()
            val currentdate = Date(currentTimeMillis)
            val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
            val formattedTime = dateFormat.format(currentdate).toString()

            val timeDifferenceMillis = calculateTimeDifference(formattedTime, enddate)
            if (timeDifferenceMillis > 0) {
                val countDownTimer = object : CountDownTimer(timeDifferenceMillis, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val secondsRemaining = millisUntilFinished / 1000
                        val timer = formatTime(secondsRemaining.toLong())
                        Timer.text = "$timer"
                    }
                    override fun onFinish() {
                        val user = FirebaseAuth.getInstance().currentUser
                        val uid = user?.uid
                        val uniqueKey = currentitem.uniqueKey
                        val database = FirebaseDatabase.getInstance()
                        val reminderTasklistRef: DatabaseReference = database.getReference("New Leads").child(uid!!)
                        val outdatedLeadsRef: DatabaseReference = database.getReference("Outdated Leads").child(uid).child(uniqueKey!!)
                        reminderTasklistRef.child(uniqueKey).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    outdatedLeadsRef.setValue(dataSnapshot.value).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            reminderTasklistRef.child(uniqueKey).removeValue()
                                        } else {
                                            Toast.makeText(holder.itemView.context,"Database Error,Contact Admin",Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                else
                                {
                                    Toast.makeText(holder.itemView.context,"Database Error,Contact Admin",Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Toast.makeText(holder.itemView.context,"Database Error,Contact Admin",Toast.LENGTH_LONG).show()
                            }
                        })

                    }
                }.start()
            }
            else {

            }




            bottomSheetDialog.setContentView(bottomSheetView)
            val behavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
            bottomSheetView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
            behavior.state = BottomSheetBehavior.STATE_EXPANDED


            bottomSheetDialog.setOnShowListener { dialog ->
                val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                bottomSheet?.let {
                    val behavior = BottomSheetBehavior.from(it)
                    // Adjust the height of the bottom sheet to match the layout's height
                    it.layoutParams.height = bottomSheetView.height
                    it.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bottom_sheet_background)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            bottomSheetDialog.show()
        }

        holder.followupButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, FinalformActivity::class.java)
            intent.putExtra("UniqueKey", currentitem.uniqueKey)
            intent.putExtra("TDate", currentitem.task_generated_date)
            intent.putExtra("TTime", currentitem.ttime)
            intent.putExtra("SchoolName", currentitem.school_name)
            intent.putExtra("SchoolEmail", currentitem.school_email)
            intent.putExtra("SchoolPhone", currentitem.school_phone_number)
            intent.putExtra("SchoolAddress", currentitem.school_address)
            intent.putExtra("SchoolPincode", currentitem.school_pincode)
            intent.putExtra("NoOfStudents", currentitem.no_of_students)
            intent.putExtra("ContactPersonName", currentitem.contact_person_name)
            intent.putExtra("ContactPersonPhone", currentitem.contact_person_phone)
            intent.putExtra("ContactPersonEmail", currentitem.contact_person_email)
            intent.putExtra("DesicionName", currentitem.desicion_maker_name)
            intent.putExtra("DesicionPhone", currentitem.desicion_maker_phone)
            intent.putExtra("DesicionEmail", currentitem.desicion_maker_email)
            intent.putExtra("DesicionDesignation", currentitem.desicion_maker_designation)
            intent.putExtra("CurrentSupplier", currentitem.current_supplier)
            intent.putExtra("CurrentSupplierDiscount", currentitem.current_supplier_discount_percentage)
            intent.putExtra("Status", currentitem.status)
            intent.putExtra("FollowUpDate", currentitem.follow_up_date)
            intent.putExtra("Prospect", currentitem.prospect)
            holder.itemView.context.startActivity(intent)
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

        val followUpDate = currentitem.follow_up_timestamp!!.toString()
        val currentTimeMillis = System.currentTimeMillis()
        val date = Date(currentTimeMillis)
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val formattedTime = dateFormat.format(date).toString()

        val timeDifferenceMillis = calculateTimeDifference(formattedTime, followUpDate)
        if (timeDifferenceMillis > 0) {
            val countDownTimer = object : CountDownTimer(timeDifferenceMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    val timer = formatTime(secondsRemaining.toLong())
                    holder.countdownTimerTextView.text = "Timer : $timer"
                }
                override fun onFinish() {


                }
            }.start()
        }
        else {

        }

    }

    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    override fun getItemCount(): Int {
        return upcommingtasklist.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun searchDataList(searchList: ArrayList<NewLeadDataClass>) {
        upcommingtasklist = searchList
        notifyDataSetChanged()
    }


    class myViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val schoolname : TextView = itemView.findViewById(R.id.UschoolnameTxt)
        //val schoolphoneno : TextView = itemView.findViewById(R.id.Uschoolphonenotxt)
        //val schoolemail : TextView = itemView.findViewById(R.id.UschoolemailTxt)
        //val schooladdress : TextView = itemView.findViewById(R.id.Uschooladdresstxt)
        //val noofstudents : TextView = itemView.findViewById(R.id.UnoofstudentsTxt)
        val prospect : TextView = itemView.findViewById(R.id.UprospectTxt)
        val followupdate : TextView = itemView.findViewById(R.id.UfollowupdateTxt)
        //val currentsupplier : TextView = itemView.findViewById(R.id.Ucurrentsuptxt)
        //val currentsupplierdiscount : TextView = itemView.findViewById(R.id.UcurrentsupplierdiscountTxt)
        //val contactpersonname : TextView = itemView.findViewById(R.id.UcontactnameTxt)
        //val contactpersonphone : TextView = itemView.findViewById(R.id.UcontactphoneTxt)
        //val contactpersonemail : TextView = itemView.findViewById(R.id.UcontatemailTxt)
        //val desicionmakername : TextView = itemView.findViewById(R.id.UdesicionmakenameTxt)
        //val desicionmakerphone : TextView = itemView.findViewById(R.id.UdesicionmakephoneTxt)
        //val desicionmakeremail : TextView = itemView.findViewById(R.id.UdescisonmakeremailTxt)
       // val desicionmakerdesignation : TextView = itemView.findViewById(R.id.UdesicionmakedesignationTxt)
        val date : TextView = itemView.findViewById(R.id.UcreateddateTxt)
        //val status : TextView = itemView.findViewById(R.id.statusTxt)
        //val moredetails : LinearLayout = itemView.findViewById(R.id.moredetailslinearlayout)
        //val additionaldeatils : LinearLayout = itemView.findViewById(R.id.addictiondeatilsLinearlayout)
        val countdownTimerTextView : TextView = itemView.findViewById(R.id.countdownTimerTextView)

        val viewdetailsButton : Button = itemView.findViewById(R.id.viewdetailsButton)
        val followupButton : Button = itemView.findViewById(R.id.followupButton)



    }

    fun clearData() {
        upcommingtasklist.clear()
        notifyDataSetChanged()
    }
    fun updateData(newData: List<NewLeadDataClass>) {
        upcommingtasklist.clear()
        upcommingtasklist.addAll(newData)
        notifyDataSetChanged()
    }

    fun calculateTimeDifference(startDate: String, endDate: String): Long {
        try {
            val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
            val startDateParsed = dateFormat.parse(startDate)
            val endDateParsed = dateFormat.parse(endDate)
            if (startDateParsed > endDateParsed){
                return 0
            }
            return Math.abs(endDateParsed.time - startDateParsed.time)
        } catch (e: Exception) {
            e.printStackTrace()
            return -1 // Return a negative value to indicate an error
        }
    }

    private fun formatTime(secondsRemaining: Long): CharSequence? {
        val hours = secondsRemaining / 3600
        val minutes = (secondsRemaining % 3600) / 60
        val seconds = secondsRemaining % 60
        return String.format("%02d:%02d:%02d",hours,minutes,seconds)

    }





}