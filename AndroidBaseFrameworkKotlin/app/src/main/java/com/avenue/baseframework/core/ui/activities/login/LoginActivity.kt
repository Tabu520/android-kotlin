package com.avenue.baseframework.core.ui.activities.login

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.activity.viewModels
import com.avenue.baseframework.R
import com.avenue.baseframework.core.helpers.EString
import com.avenue.baseframework.core.ui.activities.BaseActivity
import com.avenue.baseframework.core.ui.activities.main.MainActivity2
import com.avenue.baseframework.core.ui.activities.network.NetworkActivity
import com.avenue.baseframework.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.loginFormState.observe(this, {
            if (it == null) {
                return@observe
            }
            binding.loading.visibility = View.GONE
            when {
                it.isDataValid -> startActivity(MainActivity2::class.java)
                it.usernameError == 1 -> createDialog(
                    getString(R.string.error_login_no_logininfo),
                    getString(R.string.ok)
                )!!.show()
                else -> createDialog(
                    getString(R.string.error_login_failed),
                    getString(R.string.ok)
                )!!.show()
            }
        })
        loginViewModel.mUserLoginInfo!!.observe(this, { manPowerEntities ->
            loginViewModel.loginInfoList = manPowerEntities
            binding.buttonLogin.isEnabled = true
        })
    }

    override fun onButtonTouchUpInside(view: View?) {
        super.onButtonTouchUpInside(view)
        hideSoftKeyboard(this)
        when (view!!.id) {
            R.id.buttonLogin -> {
                val username = binding.editTextUsername.text.toString().trim { it <= ' ' }
                val password = binding.editTextPassword.text.toString().trim { it <= ' ' }
                when {
                    username == EString.EMPTY -> binding.editTextUsername.error =
                        getString(R.string.error_required)
                    password == EString.EMPTY -> binding.editTextPassword.error =
                        getString(R.string.error_required)
                    else -> {
                        binding.loading.visibility = View.VISIBLE
                        loginViewModel.login(username, password)
                    }
                }
            }
            R.id.imageButtonVisible -> {
                if (binding.editTextPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    binding.editTextPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.imageButtonVisible.setImageResource(R.drawable.outline_visibility_24)
                } else {
                    binding.editTextPassword.inputType =
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.imageButtonVisible.setImageResource(R.drawable.outline_visibility_off_24)
                }
            }
            R.id.buttonSettings -> {
                createDialog(
                    getString(R.string.warning_login_setting_configure),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    view
                )!!.show()
            }
        }
    }

    override fun onDialogPositiveClick(dialog: DialogInterface?, view: View?) {
        if (view!!.id == R.id.buttonSettings) {
            startActivity(NetworkActivity::class.java)
        }
    }
}