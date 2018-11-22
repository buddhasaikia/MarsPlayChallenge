package com.buddha.imagegallary.data.repository

import com.buddha.imagegallary.data.datasource.LocalDataSource
import com.buddha.imagegallary.data.datasource.RemoteDataSource
import com.buddha.imagegallary.data.datasource.base.BaseDataSource
import com.buddha.imagegallary.data.datasource.base.DataSource
import com.buddha.imagegallary.data.model.CloudinaryImageList
import dagger.internal.Preconditions.checkNotNull
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Buddha Saikia on 13-11-2018.
 */
@Singleton
class Repository @Inject
constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) : BaseDataSource(), DataSource {

    private val remoteDataSource: RemoteDataSource = checkNotNull(remoteDataSource)
    private val localDataSource: LocalDataSource = checkNotNull(localDataSource)

    override fun fetchImageList(): Observable<Response<CloudinaryImageList>>? {
        return remoteDataSource.fetchImageList()
    }
}
