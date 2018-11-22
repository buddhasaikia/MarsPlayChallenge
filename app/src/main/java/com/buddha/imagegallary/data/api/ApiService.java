package com.buddha.imagegallary.data.api;

import com.buddha.imagegallary.data.model.CloudinaryImageList;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by Buddha Saikia on 13-11-2018.
 */

public interface ApiService {
    @GET("image/list/marsplay.json")
    Observable<Response<CloudinaryImageList>> fetchImageList();
}
