package com.KAC.vikranthpublications.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class outdatedViewModelProvider(private val uid: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OutdatedViewModel::class.java)) {
            return OutdatedViewModel(uid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}