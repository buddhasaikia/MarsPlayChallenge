package com.buddha.imagegallary.module.base

import android.os.Bundle

import dagger.android.support.DaggerAppCompatActivity

abstract class DaggerBaseActivity : DaggerAppCompatActivity() {
    protected abstract fun getLayoutId(): Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }
}
