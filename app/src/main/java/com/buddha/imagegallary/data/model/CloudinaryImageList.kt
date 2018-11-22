package com.buddha.imagegallary.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CloudinaryImageList(
        @SerializedName("resources") @Expose var resources: List<Resource>? = null,
        @SerializedName("updated_at") @Expose var updatedAt: String? = null) : Parcelable