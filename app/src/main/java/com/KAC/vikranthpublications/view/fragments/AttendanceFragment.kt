package com.KAC.vikranthpublications.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.KAC.vikranthpublications.view.fragments.AttendanceFragment
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.KAC.vikranthpublications.R
import com.KAC.vikranthpublications.databinding.FragmentAttendanceBinding
import com.KAC.vikranthpublications.viewmodel.AttendanceViewModel
import com.KAC.vikranthpublications.viewmodel.attendanceViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class AttendanceFragment : Fragment() {
    private lateinit var attendanceViewModel: AttendanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_attendance, container, false)


        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        attendanceViewModel = ViewModelProvider(this, attendanceViewModelProvider(uid))
            .get(AttendanceViewModel::class.java)

        // Find views manually since ViewBinding is removed
        val attendanceLogoutButton: Button = view.findViewById(R.id.attendanceLogoutButton)
        val attendanceLoginButton: Button = view.findViewById(R.id.attendanceLoginButton)
        val fullnameTxt: TextView = view.findViewById(R.id.fullnameTxt)
        val employeeIDTxt: TextView = view.findViewById(R.id.employeeIDTxt)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)


        CheckLoginLoginout(attendanceLogoutButton,attendanceLoginButton)




        attendanceLoginButton.setOnClickListener {
            onLoginClick()
            attendanceLoginButton.isEnabled = false
            attendanceLogoutButton.isEnabled = true
        }

        attendanceLogoutButton.setOnClickListener {
            onLogoutClick()
            attendanceLogoutButton.isEnabled = false
        }

        setupObservers(fullnameTxt, employeeIDTxt, dateTextView)
    }

    private fun setupObservers(fullnameTxt: TextView, employeeIDTxt: TextView, dateTextView: TextView) {
        attendanceViewModel.fullName.observe(viewLifecycleOwner) { fullName ->
            fullnameTxt.text = fullName ?: "No Name"
        }

        attendanceViewModel.empid.observe(viewLifecycleOwner) { empId ->
            employeeIDTxt.text = empId ?: "Emp Code"
        }

        attendanceViewModel.date.observe(viewLifecycleOwner) { date ->
            dateTextView.text = date ?: "Date"
        }
    }


    private fun onLoginClick() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val monthFormat = DateTimeFormatter.ofPattern("MM-yyyy")
        val month = LocalDateTime.now().format(monthFormat).toString().trim()
        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val loginDate = LocalDateTime.now().format(dateFormat).toString().trim()
        if (uid != null) {
            val database = FirebaseDatabase.getInstance()
            val dateRef: DatabaseReference =
                database.getReference("User Details").child(uid!!).child("Login Details")
                    .child(month).child(loginDate)


            val currentTime = LocalTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val loginTime = currentTime.format(formatter)

            dateRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val loginTime = mutableData.child("login_time").getValue(String::class.java) ?: ""
                    mutableData.child("login_time").value = loginTime.toString()
                    return Transaction.success(mutableData)
                }

                override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                    if (committed) {
                        Log.d("Login update", "Login updated successfully.")
                    } else {
                        Log.d("Login Update", "Failed to update Login.")
                    }
                }
            })

        }
        else{
            Log.e("Database", "User not authenticated.")
        }



    }

    private fun onLogoutClick() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val monthFormat = DateTimeFormatter.ofPattern("MM-yyyy")
        val month = LocalDateTime.now().format(monthFormat).toString().trim()
        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val loginDate = LocalDateTime.now().format(dateFormat).toString().trim()
        if (uid != null) {
            val database = FirebaseDatabase.getInstance()
            val dateRef: DatabaseReference =
                database.getReference("User Details").child(uid!!).child("Login Details")
                    .child(month).child(loginDate)


            val currentTime = LocalTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val logoutTime = currentTime.format(formatter)

            dateRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val loginTime = mutableData.child("logout_time").getValue(String::class.java) ?: ""
                    mutableData.child("logout_time").value = loginTime.toString()
                    return Transaction.success(mutableData)
                }

                override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                    if (committed) {
                        Log.d("Login update", "Login updated successfully.")
                    } else {
                        Log.d("Login Update", "Failed to update Login.")
                    }
                }
            })
        }
        else{
            Log.e("Database", "User not authenticated.")
        }
    }

    private fun CheckLoginLoginout(attendanceLoginButton: Button, attendanceLogoutButton: Button) {
        val monthFormat = DateTimeFormatter.ofPattern("MM-yyyy")
        val month = LocalDateTime.now().format(monthFormat).trim()
        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val loginDate = LocalDateTime.now().format(dateFormat).trim()
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid ?: run {
            Log.e("FirebaseError", "User not authenticated.")
            return
        }

        val database = FirebaseDatabase.getInstance()
        val dateRef: DatabaseReference = database.getReference("User Details")
            .child(uid).child("Login Details").child(month).child(loginDate)

        dateRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val loginTime = dataSnapshot.child("login_time").getValue(String::class.java)
                val logoutTime = dataSnapshot.child("logout_time").getValue(String::class.java)
                Log.e("login_time", "$loginTime")
                Log.e("logout_time", "$logoutTime")
                if (loginTime == "00:00:00" ) {
                    attendanceLoginButton.isEnabled = true
                    attendanceLogoutButton.isEnabled = false
                }
                if (logoutTime == "00:00:00") {
                    attendanceLogoutButton.isEnabled = true
                    attendanceLoginButton.isEnabled = false

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FirebaseError", databaseError.message)
            }
        })
    }




}
