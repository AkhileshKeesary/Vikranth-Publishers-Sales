package com.KAC.vikranthpublications.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.KAC.vikranthpublications.model.AttendanceDataClass
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class AttendanceViewModel (private val uid: String) : ViewModel() {

    private val _enableLogoutButton = MutableLiveData<Boolean>()
    val enableLogoutButton: LiveData<Boolean> get() = _enableLogoutButton

    private val database: DatabaseReference = FirebaseDatabase.getInstance()
        .reference
        .child("User Details")
        .child(uid)
        .child("Login Details")

    private val _fullName = MutableLiveData<String>()
    private val _empid = MutableLiveData<String>()
    private val _date = MutableLiveData<String>()
    val fullName: LiveData<String> get() = _fullName
    val empid: LiveData<String> get() = _empid
    val date: LiveData<String> get() = _date



    init {
        fetchDate()
        fetchData()
        createLoginDetailsNode()
    }

    fun onLoginClick() {
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
                    val currentWallet = mutableData.child("login_time").getValue(String::class.java)?.toInt() ?: 0
                    mutableData.child("login_time").value = loginTime.toString()
                    return Transaction.success(mutableData)
                }

                override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                    if (committed) {
                        Log.d("Login update", "Login updated successfully.")
                        _enableLogoutButton.postValue(true)
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

    fun onLogoutClick() {
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
                    val currentWallet = mutableData.child("logout_time").getValue(String::class.java)?.toInt() ?: 0
                    mutableData.child("logout_time").value = logoutTime.toString()
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

    private fun fetchDate() {
        val monthFormat = DateTimeFormatter.ofPattern("MM-yyyy")
        val month = LocalDateTime.now().format(monthFormat).toString().trim()

        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val loginDate = LocalDateTime.now().format(dateFormat).toString().trim()

        _date.value = "Date : $loginDate"
    }

    private fun fetchData() {
        val auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("User Details").child(uid)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val fn = dataSnapshot.child("full_name").getValue(String::class.java)
                val empid = dataSnapshot.child("employee_code").getValue(String::class.java)
                _fullName.value = fn!!.toString()
                _empid.value = "Emp Id : $empid"
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }


    private fun createLoginDetailsNode() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        if (uid != null) {
            val database = FirebaseDatabase.getInstance()
            val userDetailsRef: DatabaseReference = database.getReference("User Details").child(uid).child("Login Details")

            // Define the date formats
            val monthFormat = DateTimeFormatter.ofPattern("MM-yyyy")
            val month = LocalDateTime.now().format(monthFormat).toString().trim()

            val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val loginDate = LocalDateTime.now().format(dateFormat).toString().trim()

            // Reference for the month node
            val monthRef = userDetailsRef.child(month)

            // Check if the month node exists
            monthRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(monthSnapshot: DataSnapshot) {
                    if (monthSnapshot.exists()) {
                        // Month node exists, check if the date node exists
                        val dateRef = monthRef.child(loginDate)
                        dateRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dateSnapshot: DataSnapshot) {
                                if (dateSnapshot.exists()) {
                                    // Node with today's date already exists
                                    Log.d("Database", "Login Details node for today already exists.")
                                } else {
                                    // Node with today's date does not exist, create it
                                    val loginTime = "00:00:00"
                                    val loginLocation = "000000"
                                    val logoutTime = "00:00:00"
                                    val totalHours = "0"
                                    val logoutLocation = "000000"
                                    val loginDetails = AttendanceDataClass(loginDate, loginTime, loginLocation, logoutTime, totalHours, logoutLocation)

                                    dateRef.setValue(loginDetails)
                                        .addOnSuccessListener {
                                            Log.d("Database", "Login Details node created successfully.")

                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("Database", "Error creating Login Details node", exception)
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("Database", "Error checking for existing date node", error.toException())
                            }
                        })
                    } else {
                        // Month node does not exist, create it
                        monthRef.setValue(null).addOnSuccessListener {
                            // Once month node is created, proceed to create the date node
                            val dateRef = monthRef.child(loginDate)
                            val loginTime = "000000"
                            val loginLocation = "000000"
                            val logoutTime = "000000"
                            val totalHours = "0"
                            val logoutLocation = "000000"
                            val loginDetails = AttendanceDataClass(loginDate, loginTime, loginLocation, logoutTime, totalHours, logoutLocation)

                            dateRef.setValue(loginDetails)
                                .addOnSuccessListener {
                                    Log.d("Database", "Login Details node created successfully.")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Database", "Error creating Login Details node", exception)
                                }
                        }.addOnFailureListener { exception ->
                            Log.e("Database", "Error creating month node", exception)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Database", "Error checking for existing month node", error.toException())
                }
            })
        } else {
            Log.e("Database", "User not authenticated.")
        }
    }



    /*private fun fetchMonthDetails() {
           database.addValueEventListener(object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                   val taskList = mutableListOf<MonthDataClass>()
                   for (dataSnapshot in snapshot.children) {
                       val monthYearKey = dataSnapshot.key
                       if (monthYearKey != null) {
                           val monthData = MonthDataClass(month_year = monthYearKey)
                           taskList.add(monthData)
                           Log.d("ViewModel", "Fetched item: $monthYearKey")
                       } else {
                           Log.d("ViewModel", "Null item fetched")
                       }
                   }

                   taskList.sortByDescending {
                       try {
                           SimpleDateFormat("MM-yyyy", Locale.getDefault()).parse(it.month_year) ?: Date()
                       } catch (e: Exception) {
                           Date()
                       }
                   }
                   _tasks.value = taskList
               }

               override fun onCancelled(error: DatabaseError) {
                   Log.e("ViewModel", "Failed to read value.", error.toException())
               }
           })
       } */


}