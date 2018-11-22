package com.buddha.imagegallary.module.details

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import com.buddha.imagegallary.R
import com.buddha.imagegallary.data.model.Resource
import com.buddha.imagegallary.module.base.DaggerBaseActivity
import com.buddha.imagegallary.util.ActivityUtils
import com.buddha.imagegallary.util.Config

class DetailsActivity : DaggerBaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_details
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom,R.anim.abc_shrink_fade_out_from_bottom)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar?.setBackgroundColor(Color.TRANSPARENT)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val window = this.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }

        val datum: Resource? = intent?.extras?.getParcelable(Config.Extras.DATUM)
        val fragment: DetailsFragment? = DetailsFragment.newInstance(datum)
        ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment ?: return, R.id.container)
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
        fun start(context: Context?, datum: Resource) {
            if (context == null) return
            val intent = Intent(context, DetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(Config.Extras.DATUM, datum)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}
