package com.avenue.baseframework.core.ui.activities.main

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.avenue.baseframework.R
import com.avenue.baseframework.core.adapters.MenuExpandableListviewAdapter
import com.avenue.baseframework.core.helpers.ChooseFragmentHelper
import com.avenue.baseframework.core.models.MenuModel
import com.avenue.baseframework.core.models.ResponseMenuJson
import com.avenue.baseframework.core.ui.activities.BaseActivity
import com.avenue.baseframework.core.ui.activities.login.LoginActivity
import com.avenue.baseframework.core.utils.StringUtils
import com.avenue.baseframework.databinding.ActivityMain2Binding
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity2 : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMain2Binding

    private var actionBar: ActionBar? = null
    private var drawer: DrawerLayout? = null
    private var toggle: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null
    private var expandableListViewMenu: ExpandableListView? = null
    private var fragmentManager: FragmentManager? = null
    private var fmActive: Fragment? = null
    private var menuExpandableListViewAdapter: MenuExpandableListviewAdapter? = null
    private val gListParentMenu: MutableList<MenuModel> = ArrayList()
    private val gListChildMenu: SparseArray<MutableList<MenuModel>> =
        SparseArray<MutableList<MenuModel>>()
    private var gTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        initView()
        initMenuNavigation()
    }

    private fun initView() {
        gTitle = findViewById(R.id.action_bar_title)
        actionBar = supportActionBar
        actionBar?.let {
            it.title = ""
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(
            this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        toggle?.let {
            it.isDrawerIndicatorEnabled = true
            drawer!!.addDrawerListener(it)
        }
        navigationView = findViewById(R.id.nav_view_manager_menu)
        expandableListViewMenu = findViewById(R.id.expandablelistview_menu)
        navigationView!!.setNavigationItemSelectedListener(this)
        fragmentManager = supportFragmentManager
    }

    private fun initMenuNavigation() {
        addListParentChildMenu()
        menuExpandableListViewAdapter =
            MenuExpandableListviewAdapter(this, gListParentMenu, gListChildMenu)
        expandableListViewMenu?.let {
            it.setAdapter(menuExpandableListViewAdapter)
            it.setOnChildClickListener { _: ExpandableListView?, _: View?, groupPosition: Int, childPosition: Int, _: Long ->
                val nodeClick =
                    menuExpandableListViewAdapter!!.getChild(
                        groupPosition,
                        childPosition
                    ) as MenuModel
                if (nodeClick.level == 2) {
                    startActivityFromButton(nodeClick.webscreen, nodeClick.webscreenName)
                    drawer!!.closeDrawers()
                }
                false
            }
            it.setOnGroupClickListener { _: ExpandableListView?, _: View?, groupPosition: Int, _: Long ->
                val nodeClick =
                    menuExpandableListViewAdapter!!.getGroup(groupPosition) as MenuModel
                if (nodeClick.level == 1 && nodeClick.menuId == 8) {
                    startActivityFromButton(nodeClick.webscreen, nodeClick.webscreenName)
                }
                false
            }
        }
    }

    private fun addListParentChildMenu() {
        val urlMenuJson: String = StringUtils.getFilesDir() + "/android_vs100/menu.txt"
        if (File(urlMenuJson).exists()) {
            val response: ResponseMenuJson = Gson().fromJson(
                StringUtils.getJsonAssetsExternalStoragePath(urlMenuJson),
                ResponseMenuJson::class.java
            )
            for (i in response.data) {
                gListParentMenu.add(i)
                val listChild = ArrayList<MenuModel>()
                for ((menuId, webscreen, webscreenName, iconMenu, parentID, level) in i.listChild) {
                    gListChildMenu.put(i.menuId, listChild)
                }
            }
        } else {
            gListParentMenu.add(
                MenuModel(1, "mHome", "Trang chủ", getIDResourceLeftMenu("mHome"), 0, 1)
            )
            gListParentMenu.add(
                MenuModel(2, "mLogbook", "Logbook", getIDResourceLeftMenu("mLogbook"), 0, 1)
            )
            gListParentMenu.add(
                MenuModel(3, "mLogSheet", "LogSheet", getIDResourceLeftMenu("mLogSheet"), 0, 1)
            )
            gListParentMenu.add(
                MenuModel(4, "mOtherApp", "Ứng dụng khác", getIDResourceLeftMenu("mOtherApp"), 0, 1)
            )
            gListParentMenu.add(
                MenuModel(5, "mSystem", "Hệ thống", getIDResourceLeftMenu("mSystem"), 0, 1)
            )

            // Chill Menu
            val listChildLogbook: MutableList<MenuModel> = ArrayList()
            listChildLogbook.add(
                MenuModel(21, "mLogbook_WriteLogbook", "Ghi Logbook", getIDResourceLeftMenu("mLogbook_WriteLogbook"), 2, 2)
            )
            listChildLogbook.add(
                MenuModel(22, "mLogbook_ReadLogbook", "Xem Logbook", getIDResourceLeftMenu("mLogbook_ReadLogbook"), 2, 2)
            )
            gListChildMenu.put(listChildLogbook[0].parentID, listChildLogbook)
            val listChildLogSheet: MutableList<MenuModel> = ArrayList()
            listChildLogSheet.add(
                MenuModel(31, "mLogbook_WriteLogSheet", "Ghi LogSheet", getIDResourceLeftMenu("mLogbook_WriteLogSheet"), 3, 2)
            )
            //listChildLogSheet.add(MenuModel(32, "mLogbook_ReadLogSheet", "Xem LogSheet", getIDResourceLeftMenu("mLogbook_ReadLogSheet"), 3, 2))
            gListChildMenu.put(listChildLogSheet[0].parentID, listChildLogSheet)
            val listChildSystem: MutableList<MenuModel> = ArrayList()
            listChildSystem.add(
                MenuModel(51, "mSystem_Configuration", "Cấu hình", getIDResourceLeftMenu("mSystem_Configuration"), 5, 2)
            )
            listChildSystem.add(
                MenuModel(52, "mSystem_Information", "Thông tin", getIDResourceLeftMenu("mSystem_Information"), 5, 2)
            )
            listChildSystem.add(
                MenuModel(53, "mSystem_Logout", "Đăng xuất", getIDResourceLeftMenu("mSystem_Logout"), 5, 2)
            )
            gListChildMenu.put(listChildSystem[0].parentID, listChildSystem)
        }
    }

    private fun startActivityFromButton(menuCode: String, menuName: String) {
        var _fragment: Fragment? = null
        when (menuCode) {
            "mSystem_Logout" -> logout()
            else -> _fragment = ChooseFragmentHelper.getFragmentByMenuItem(menuCode)
        }
        if (menuCode != "mSystem_Logout") {
            loadFragment(_fragment, menuName)
        }
    }

    private fun loadFragment(fragment: Fragment?, title: String): Boolean {
        fragment?.let {
            /*if (fragment is HomeFragment) {
                toggle?.isDrawerIndicatorEnabled = true

            } else {
                toggle?.isDrawerIndicatorEnabled = false
                actionBar!!.setHomeButtonEnabled(true)
                drawer?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }*/
            gTitle!!.text = title
            if (fmActive !== it) {

                /*val transaction = fragmentManager.beginTransaction().hide(fm_active).show(fragment)
                transaction.detach(fragment)
                transaction.attach(fragment)
                transaction.commit()*/
                val transaction =
                    fragmentManager!!.beginTransaction().replace(R.id.nav_host_fragment, it)
                transaction.commit()

                //fragmentManager.beginTransaction().hide(fm_active).show(fragment).commit();
                //fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).hide(fm_active).show(fragment).commit();
                fmActive = it
            }

            /*getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(fragment.getTag())
                    .replace(R.id.fragment_container_menu_manager, fragment)
                    .commit();*/
            return true
        }
        return false
    }

    private fun getIDResourceLeftMenu(webScreen: String): Int {
        var id = 0
        when (webScreen) {
            "mHome" -> id = R.drawable.ic_baseline_keyboard_arrow_down_24
            "mLogbook" -> id = R.drawable.ic_baseline_keyboard_arrow_down_24
            "mLogSheet" -> id = R.drawable.ic_baseline_keyboard_arrow_down_24
            "mOtherApp" -> id = R.drawable.ic_baseline_keyboard_arrow_down_24
            "mSystem" -> id = R.drawable.ic_baseline_keyboard_arrow_down_24
        }
        return id
    }

    private fun logout() {
        drawer!!.closeDrawers()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return false
    }
}