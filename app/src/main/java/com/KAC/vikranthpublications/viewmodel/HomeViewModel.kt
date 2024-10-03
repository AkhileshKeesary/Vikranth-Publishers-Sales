package com.KAC.vikranthpublications.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.KAC.vikranthpublications.model.NewLeadDataClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(private val uid: String) : ViewModel() {

    private val _tasks = MutableLiveData<List<NewLeadDataClass>>()
    val tasks: LiveData<List<NewLeadDataClass>> get() = _tasks

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("New Leads").child(uid)

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskList = mutableListOf<NewLeadDataClass>()
                for (dataSnapshot in snapshot.children) {
                    val task = dataSnapshot.getValue(NewLeadDataClass::class.java)
                    if (task != null) {
                        taskList.add(task)
                    }
                }
                taskList.sortByDescending {
                    try {
                        SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).parse(it.ttime ?: "") ?: Date()
                    } catch (e: Exception) {
                        Date()
                    }
                }

                _tasks.value = taskList

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }
}