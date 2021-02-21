package com.example.pan

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.pan.ui.dashboard.DashboardFragment
import com.example.pan.ui.home.HomeFragment
import com.example.pan.ui.notifications.NotificationsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbruyelle.rxpermissions3.RxPermissions

class MainActivity : AppCompatActivity() {

    private lateinit var mBinder: Aria2Service.Aria2Binder

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mBinder = service as Aria2Service.Aria2Binder
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
        viewPager.requestDisallowInterceptTouchEvent(true)
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = object : FragmentStatePagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getCount(): Int = 3

            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> HomeFragment()
                    1 -> DashboardFragment()
                    else -> NotificationsFragment()
                }
            }

        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                bottomNavigationView.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> viewPager.setCurrentItem(0, false)
                R.id.navigation_dashboard -> viewPager.setCurrentItem(1, false)
                R.id.navigation_notifications -> viewPager.setCurrentItem(2, false)
            }
            false
        }

        val permissions = RxPermissions(this)
        permissions.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe {
            if (it) {
                val aria2Service = Intent(this, Aria2Service::class.java)
                startService(aria2Service)
                bindService(aria2Service, connection, Context.BIND_AUTO_CREATE)
            } else {
                Toast.makeText(applicationContext, "权限申请失败", Toast.LENGTH_SHORT).show()
            }
        }


    }
}