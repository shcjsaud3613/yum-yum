package com.omnyom.yumyum

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.omnyom.yumyum.databinding.ActivityMainBinding
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.PreferencesManager.Companion.eurekaDistance
import com.omnyom.yumyum.helper.PreferencesManager.Companion.pushOn
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import com.omnyom.yumyum.ui.feed.CameraActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun extraSetupBinding() { }

    override fun setup() {
        KakaoMapUtils.initLocationManager(this)
        userId = PreferencesManager.getLong(this, "userId")?: 0
        eurekaDistance = PreferencesManager.getLong(this, "eurekaDistance")?: 4
        pushOn = PreferencesManager.getLong(this, "pushOn")?: 1

        Firebase.messaging.subscribeToTopic("$userId")
                .addOnCompleteListener { task ->
                    var msg = "성공"
                    if (!task.isSuccessful) {
                        msg = "실패"
                    }
                }
    }

    override fun setupViews() {
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.menu.findItem(R.id.navigation_write).isCheckable = false
        binding.navView.menu.findItem(R.id.navigation_write).setOnMenuItemClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            true
        }
        supportActionBar?.hide()
    }

    override fun onSubscribe() { }

    override fun release() { }
}



