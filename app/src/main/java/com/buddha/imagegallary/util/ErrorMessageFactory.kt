package com.buddha.imagegallary.util

import android.accounts.NetworkErrorException
import android.content.Context

import com.buddha.imagegallary.BuildConfig
import com.buddha.imagegallary.R

import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorMessageFactory(private val context: Context?) {
    private val defaultMessage = "Something went wrong!"

    fun getError(t: Throwable?): String {
        t?.printStackTrace()
        val msg = if ((t is ConnectException) or (t is UnknownHostException) or (t is NetworkErrorException)) {
            context?.resources?.getString(R.string.internet_error) ?: defaultMessage
        } else if (t is SocketTimeoutException || t is NoRouteToHostException) {
            context?.resources?.getString(R.string.error_unable_to_connect_to_the_server_try_again) ?: defaultMessage
        } else {
            context?.getString(R.string.error_something_went_wrong_try_again) ?: defaultMessage
        }
        //Firebase additional crash reporting
        if (!BuildConfig.DEBUG) {
            //FirebaseCrash.report(t);
        }
        return msg
    }
}
