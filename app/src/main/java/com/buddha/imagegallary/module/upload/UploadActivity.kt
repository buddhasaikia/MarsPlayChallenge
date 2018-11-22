package com.buddha.imagegallary.module.upload

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.buddha.imagegallary.R
import com.buddha.imagegallary.module.base.DaggerBaseActivity
import com.buddha.imagegallary.util.ActivityUtils
import com.buddha.imagegallary.util.Config
import com.yalantis.ucrop.UCrop

class UploadActivity : DaggerBaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_upload
    }

    private lateinit var fragment: UploadFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter, R.anim.leave)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fragment = UploadFragment()
        ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.container)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UploadActivity::class.java)
            context.startActivity(intent)
        }

        fun startForResult(context: Context) {
            if(context !is Activity) return

            val intent = Intent(context, UploadActivity::class.java)
            context.startActivityForResult(intent, Config.StartActivityForResult.UPLOAD)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==Config.StartActivityForResult.CAPTURE_ACTIVITY_REQUEST_CODE
                        ||requestCode==Config.StartActivityForResult.PICK_IMAGE_REQUEST
                        ||requestCode==UCrop.REQUEST_CROP) {
            fragment.onActivityResult(requestCode, resultCode, data)
        } else
            super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right_enter, R.anim.left_to_right_exit)
    }
}
