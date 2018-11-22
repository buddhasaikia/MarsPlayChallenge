package com.buddha.imagegallary.module.base

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {
    protected abstract var rootView: View

    protected abstract fun getLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    fun isAlive(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing
    }
}
