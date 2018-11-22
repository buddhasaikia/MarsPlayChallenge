package com.buddha.imagegallary.module.home

import com.buddha.imagegallary.data.datasource.base.BaseViewModel
import com.buddha.imagegallary.data.datasource.base.DataSource
import com.buddha.imagegallary.data.model.CloudinaryImageList
import com.buddha.imagegallary.data.repository.Repository

import io.reactivex.Observable
import retrofit2.Response

class HomeViewModel(private val repository: Repository) : BaseViewModel(), DataSource {
    override fun fetchImageList(): Observable<Response<CloudinaryImageList>>? {
        return repository.fetchImageList()
    }

}
