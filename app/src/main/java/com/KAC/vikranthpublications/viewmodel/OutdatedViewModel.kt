package com.KAC.vikranthpublications.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.KAC.vikranthpublications.model.NewLeadDataClass
import com.KAC.vikranthpublications.model.OutdatedDataClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OutdatedViewModel(private val uid: String) : ViewModel() {

    private val _tasks = MutableLiveData<List<OutdatedDataClass>>()
    val tasks: LiveData<List<OutdatedDataClass>> get() = _tasks

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("Outdated Leads").child(uid)

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskList = mutableListOf<OutdatedDataClass>()
                for (dataSnapshot in snapshot.children) {
                    val task = dataSnapshot.getValue(OutdatedDataClass::class.java)
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