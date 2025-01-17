package com.KAC.vikranthpublications.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UpcommingViewModelProvider(private val uid: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(uid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}