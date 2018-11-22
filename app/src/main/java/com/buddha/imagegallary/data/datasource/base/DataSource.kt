package com.buddha.imagegallary.data.datasource.base


import com.buddha.imagegallary.data.model.CloudinaryImageList
import io.reactivex.Observable
import retrofit2.Response

/**
 * Created by Buddha Saikia on 13-11-2018.
 */

interface DataSource {

    fun fetchImageList(): Observable<Response<CloudinaryImageList>>?
}
