package com.omnyom.yumyum.ui.useroption

import android.content.Intent
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityMyOptionBinding
import com.omnyom.yumyum.helper.GoogleLoginHelper
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import com.omnyom.yumyum.ui.login.LoginActivity

class MyOptionActivity : BaseBindingActivity<ActivityMyOptionBinding>(R.layout.activity_my_option) {
    override fun extraSetupBinding() { }

    override fun setup() { }

    override fun setupViews() {
        binding.btnLogout.setOnClickListener { signOut() }
        binding.btnEditProfile.setOnClickListener { goUserEdit() }
        binding.btnInfoAgreement.setOnClickListener { goInfoAgreement() }
        binding.btnEurekaOption.setOnClickListener { goEurekaOption() }
        binding.btnBack.setOnClickListener { finish() }

        supportActionBar?.hide()
    }

    override fun onSubscribe() { }

    override fun release() { }

    private fun goInfoAgreement() {
        binding.wvInfoAgreement.loadUrl(getString(R.string.privacy_policy_site))
    }

    private fun signOut() {
        GoogleLoginHelper.firebaseAuth.signOut()
        GoogleLoginHelper.googleSignOut(this) { startLoginActivity() }
    }

    private fun startLoginActivity() {
        Intent(this, LoginActivity::class.java).let {
            startActivity(it)
        }
        this.finish()
    }

    private fun goUserEdit() {
        Intent(this, UserInfoEditActivity::class.java).let {
            startActivity(it)
        }
    }

    private fun goEurekaOption() {
        Intent(this, EurekaOptionActivity::class.java).let {
            startActivity(it)
        }
    }
}