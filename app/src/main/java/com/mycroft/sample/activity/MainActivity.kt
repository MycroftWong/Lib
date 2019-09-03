package com.mycroft.sample.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.OnTitleBarListener
import com.mycroft.sample.R
import com.mycroft.sample.common.CommonActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CommonActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun getResId(): Int {
        return R.layout.activity_main
    }

    override fun initFields(savedInstanceState: Bundle?) {
    }

    override fun initViews() {

        immersionBar {
            statusBarColor(R.color.colorPrimaryDark)
            fitsSystemWindows(true)
            statusBarDarkFont(true)
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)


        titleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: View) {}

            override fun onTitleClick(v: View) {}

            override fun onRightClick(v: View) {
                startActivity(SearchActivity.getIntent(this@MainActivity))
            }
        })
    }

    override fun loadData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(this).clearMemory()
    }
}
