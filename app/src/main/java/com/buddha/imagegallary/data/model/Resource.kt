package com.buddha.imagegallary.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Resource(@SerializedName("public_id") @Expose var publicId: String? = null,
                    @SerializedName("version") @Expose var version: Int? = null,
                    @SerializedName("format") @Expose var format: String? = null,
                    @SerializedName("width") @Expose var width: Int? = null,
                    @SerializedName("height") @Expose var height: Int? = null,
                    @SerializedName("type") @Expose var type: String? = null,
                    @SerializedName("created_at") @Expose var createdAt: String? = null):Parcelable