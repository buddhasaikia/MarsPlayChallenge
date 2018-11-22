package com.buddha.imagegallary.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.buddha.imagegallary.data.repository.Repository
import com.buddha.imagegallary.module.home.HomeViewModel
import com.buddha.imagegallary.module.upload.UploadViewModel

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomViewModelFactory @Inject
constructor(private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            HomeViewModel(repository) as T
        else if (modelClass.isAssignableFrom(UploadViewModel::class.java))
            UploadViewModel(repository) as T
        else
            throw IllegalArgumentException("ViewModel not found!")
    }
}
