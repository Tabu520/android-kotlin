package com.avenue.baseframework.core.ui.activities.main

import android.content.pm.PackageManager
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.avenue.baseframework.R
import com.avenue.baseframework.core.helpers.AppSize
import com.avenue.baseframework.core.ui.activities.BaseActivity
import com.avenue.baseframework.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private var mAppBarConfiguration: AppBarConfiguration? = null

    @Inject
    lateinit var appSize: AppSize

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        binding.navView.itemIconTintList = null
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home,
            R.id.nav_template,
            R.id.nav_list_book,
            R.id.nav_logsheet_templates,
            R.id.nav_list_sheet,
            R.id.nav_manpowers,
            R.id.nav_settings,
            R.id.nav_logout
        )
            .setOpenableLayout(binding.drawerLayout)
            .build()
        val navController = findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(binding.navView, navController)
        binding.drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {
                hideSoftKeyboard(this@MainActivity)
            }
        })

        getDisplayMetrics()
        setSoftInputMode()
    }

    override fun onBackPressed() {
        setSoftInputMode()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_logout -> {
                startActivity(LoginActivity::class.java)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        setSoftInputMode()
        val navController = findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, mAppBarConfiguration!!) || super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(Companion.TAG, "Permission: " + permissions[0] + "was " + grantResults[0])
            createDialog("Bấm nút 'Gửi' lần nữa để gửi email", getString(R.string.ok))
        } else {
            createDialog("Bạn cần cấp phép truy để gửi email", getString(R.string.ok))
        }
    }

    private fun getDisplayMetrics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            appSize.screenHeight = windowMetrics.bounds.height() - insets.bottom - insets.top
            appSize.screenWidth = windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            appSize.screenHeight = displayMetrics.heightPixels
            appSize.screenWidth = displayMetrics.widthPixels
        }
    }

    private fun registerUpdateReceiver() {
//        networkChangeReceiver = NetworkChangeReceiver()
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(Constants.ACTION_CONNECTIVITY_CHANGE)
//        registerReceiver(networkChangeReceiver, intentFilter)
    }

    private fun unregisterUpdateReceiver() {
//        unregisterReceiver(networkChangeReceiver)
    }

    private fun setSoftInputMode() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}