package com.buddha.imagegallary.data.datasource


import com.buddha.imagegallary.data.api.ApiService
import com.buddha.imagegallary.data.datasource.base.BaseDataSource
import com.buddha.imagegallary.data.datasource.base.DataSource
import com.buddha.imagegallary.data.model.CloudinaryImageList

import io.reactivex.Observable
import retrofit2.Response

/**
 * Created by Buddha Saikia on 13-11-2018.
 */

class RemoteDataSource(private val apiService: ApiService) : BaseDataSource(), DataSource {

    override fun fetchImageList(): Observable<Response<CloudinaryImageList>>? {
        return apiService.fetchImageList().compose(applySchedulersIO())
    }
}
