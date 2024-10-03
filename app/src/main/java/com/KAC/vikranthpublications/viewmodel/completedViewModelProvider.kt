package com.KAC.vikranthpublications.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class completedViewModelProvider(private val uid: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletedleadsViewModel::class.java)) {
            return CompletedleadsViewModel(uid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}