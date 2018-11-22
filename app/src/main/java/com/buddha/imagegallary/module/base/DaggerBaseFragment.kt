package com.buddha.imagegallary.module.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import dagger.android.support.DaggerFragment


abstract class DaggerBaseFragment : DaggerFragment() {
    protected var rootView: View?=null

    protected abstract fun getLayoutId():Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    fun isAlive(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing
    }

}
