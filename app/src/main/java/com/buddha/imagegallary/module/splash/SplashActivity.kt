package com.buddha.imagegallary.module.splash

import android.app.Activity
import android.os.Bundle
import com.buddha.imagegallary.module.home.HomeActivity

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HomeActivity.start(this)
        this.finish()
    }
}
