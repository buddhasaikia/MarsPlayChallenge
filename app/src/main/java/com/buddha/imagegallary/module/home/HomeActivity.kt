package com.buddha.imagegallary.module.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import com.buddha.imagegallary.R
import com.buddha.imagegallary.module.base.DaggerBaseActivity
import com.buddha.imagegallary.module.upload.UploadActivity
import com.buddha.imagegallary.util.ActivityUtils
import com.buddha.imagegallary.util.Config
import com.buddha.imagegallary.util.Utils
import kotlinx.android.synthetic.main.app_bar_home.*
import javax.inject.Inject

class HomeActivity : DaggerBaseActivity() {
    @Inject lateinit var utils:Utils
    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.abc_shrink_fade_out_from_bottom, R.anim.abc_grow_fade_in_from_bottom)
    }

    private lateinit var mainFragment: HomeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            UploadActivity.startForResult(this)
        }

        mainFragment = HomeFragment.newInstance()
        ActivityUtils.addFragmentToActivity(supportFragmentManager, mainFragment, R.id.container)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_upload -> UploadActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context?) {
            if (context !is Activity) return
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Config.StartActivityForResult.UPLOAD) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getStringExtra("result")
                when (result) {
                    "success" -> mainFragment.onRefresh()
                    else -> {
                        utils.showErrorDialog("Something went wrong! Please try again later.")
                        return
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
