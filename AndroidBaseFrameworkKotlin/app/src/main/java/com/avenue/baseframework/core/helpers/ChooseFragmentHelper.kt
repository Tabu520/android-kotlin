package com.avenue.baseframework.core.helpers

import androidx.fragment.app.Fragment
import com.avenue.baseframework.core.ui.fragments.home.HomeFragment

object ChooseFragmentHelper {

    fun getFragmentByMenuItem(item: String?): Fragment? {
        var fragment: Fragment? = null
        when (item) {
            "mLogbook_WriteLogbook" -> fragment = HomeFragment()
            "mLogbook_ReadLogbook" -> {}
            "mLogbook_WriteLogSheet" -> {
                fragment = HomeFragment()
            }
            else -> fragment = HomeFragment()
        }
        return fragment
    }
}