package com.avenue.baseframework.core.ui.activities.login

import android.os.Bundle
import com.avenue.baseframework.R
import com.avenue.baseframework.core.ui.activities.BaseActivity

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}