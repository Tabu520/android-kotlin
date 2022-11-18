package com.taipt.tiktokdownloaderapp.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.taipt.tiktokdownloaderapp.R
import com.taipt.tiktokdownloaderapp.databinding.ActivityMainBinding
import com.taipt.tiktokdownloaderapp.ui.fragments.LanguageFragment
import com.taipt.tiktokdownloaderapp.ui.fragments.RateAppBottomSheetDialog
import com.taipt.tiktokdownloaderapp.utils.Logger
import com.taipt.tiktokdownloaderapp.utils.PermissionsChecking
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigInteger
import java.security.MessageDigest


@AndroidEntryPoint
class MainActivity : BaseActivity(), LanguageFragment.LanguageFragmentListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        PermissionsChecking.checkStoragePermissions(this)


        binding.toolbar.overflowIcon = ContextCompat.getDrawable(this, R.color.gray_dark)
        binding.toolbar.title = ""
        binding.toolbar.subtitle = ""
        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

        navController = findNavController(R.id.navHostFragment)
        val navView: NavigationView = binding.navigationView
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)

        navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.rate_the_app -> {
                    var mAndroid = Secure.getString(contentResolver, Secure.ANDROID_ID)
                    Logger.logMessage("ID -- $mAndroid")
                    val text = mAndroid + "azsdf&#@12"
                    val crypt = MessageDigest.getInstance("MD5")
                    crypt.digest(text.toByteArray(Charsets.UTF_8));
                    val digest = BigInteger(1, crypt.digest()).toString(16).padStart(32, '0')
                    Logger.logMessage("Sign -- $digest")
                    RateAppBottomSheetDialog().apply {
                        show(supportFragmentManager, RateAppBottomSheetDialog.TAG)
                    }
                }
                R.id.feedback -> sendFeedback()
                R.id.faqFragment -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_downloadFragment2_to_faqFragment)
                }
                R.id.share_with_friends -> {
                    shareApplication()
                }
                R.id.languageFragment -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_downloadFragment2_to_languageFragment)
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        binding.ibRateApp.setOnClickListener {
            Toast.makeText(this, "Rate App", Toast.LENGTH_SHORT).show()
            RateAppBottomSheetDialog().apply {
                show(supportFragmentManager, RateAppBottomSheetDialog.TAG)
            }
        }

        binding.ibDownloadedVideo.setOnClickListener {
            Toast.makeText(this, "Download Video", Toast.LENGTH_SHORT).show()
            findNavController(R.id.navHostFragment).navigate(R.id.action_downloadFragment2_to_videosFragment)
        }

        listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.videosFragment, R.id.faqFragment, R.id.languageFragment -> {
                    binding.tvMp3.visibility = View.INVISIBLE
                    binding.tvTik.visibility = View.INVISIBLE
                    binding.ibDownloadedVideo.visibility = View.GONE
                    binding.ibRateApp.visibility = View.GONE
                }
                else -> {
                    binding.toolbar.title = ""
                    binding.toolbar.subtitle = ""
                    binding.tvMp3.visibility = View.VISIBLE
                    binding.tvTik.visibility = View.VISIBLE
                    binding.ibDownloadedVideo.visibility = View.VISIBLE
                    binding.ibRateApp.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(listener)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun sendFeedback() {
        val i = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("recipient@example.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback for Download Tiktok Video App")
        }

        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "There are no email clients installed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun shareApplication() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Let me recommend you this app: link app")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onSettingLanguageDone(language: String) {
        Logger.logMessage("onSettingLanguageDone -- $language")
        val intent = intent
        finish()
        startActivity(intent)
    }

}