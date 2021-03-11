package com.example.pan

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.pan.aria2.Aria2Service
import com.example.pan.databinding.ActivityMainBinding
import com.example.pan.ui.file.ListFileFragment
import com.example.pan.ui.home.HomeFragment
import com.example.pan.ui.login.LoginActivity
import com.example.pan.ui.notifications.NotificationsFragment
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mBinder: Aria2Service.Aria2Binder
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mBinder = service as Aria2Service.Aria2Binder
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        mainViewModel.getUserInfo()

        initViewAndListener()


        RxPermissions(this).request(
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

    private fun initViewAndListener() = with(binding) {
        val actionBarDrawerToggle =
            ActionBarDrawerToggle(
                this@MainActivity,
                binding.drawerLayout,
                binding.toolbar,
                R.string.open,
                R.string.close
            )
        actionBarDrawerToggle.syncState()

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
                    1 -> ListFileFragment()
                    else -> NotificationsFragment()
                }
            }

        }
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                navView.menu.getItem(position).isChecked = true
            }
        })
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    viewPager.setCurrentItem(0, false)
                    toolbar.title = getString(R.string.title_home)
                }
                R.id.nav_file -> {
                    viewPager.setCurrentItem(1, false)
                    toolbar.title = getString(R.string.title_file)
                }
                R.id.nav_download -> {
                    viewPager.setCurrentItem(2, false)
                    toolbar.title = getString(R.string.title_download)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        mainViewModel.title.observe(this@MainActivity) {
            toolbar.title = it
        }

        val headerView = navView.getHeaderView(0)
        val headerImg = headerView.findViewById<ImageView>(R.id.headimgurl)
        val userName = headerView.findViewById<TextView>(R.id.user_name)
        val quato = headerView.findViewById<TextView>(R.id.quato)
        val progressBar = headerView.findViewById<ProgressBar>(R.id.progress_bar)

        headerView.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        mainViewModel.userInfo.observe(this@MainActivity) {


            userName.text = it.netdisk_name
            quato.text = "${it.used}/${it.total}"
            progressBar.progress = it.percent

            Glide.with(this@MainActivity).load(it.avatar_url).into(headerImg)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val aria2Service = Intent(this, Aria2Service::class.java)
        stopService(aria2Service)
    }
}