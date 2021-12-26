package com.blesspearl.jobz4me.VMFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blesspearl.jobz4me.ViewModels.HomeActivityViewModel

class HAViewModelFactory:ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeActivityViewModel::class.java)){
            return  HomeActivityViewModel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }
}