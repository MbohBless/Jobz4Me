package com.blesspearl.jobz4me.VMFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blesspearl.jobz4me.ViewModels.UViewModel

class UViewModelFactory():ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UViewModel::class.java)){
            return  UViewModel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }
}