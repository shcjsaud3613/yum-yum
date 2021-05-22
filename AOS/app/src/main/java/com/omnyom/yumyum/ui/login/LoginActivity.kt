package com.omnyom.yumyum.ui.login

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.auth.api.Auth
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityLoginBinding
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.RESULT_CODE
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.firebaseAuth
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.getGoogleSignInIntent
import com.omnyom.yumyum.helper.GoogleLoginHelper.Companion.googleSignIn
import com.omnyom.yumyum.helper.KakaoMapUtils.Companion.PERM_COARSE_LOCATION
import com.omnyom.yumyum.helper.KakaoMapUtils.Companion.PERM_FINE_LOCATION
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import com.omnyom.yumyum.ui.feed.SearchPlaceActivity
import com.omnyom.yumyum.ui.signup.SignUpActivity


class LoginActivity: BaseBindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val loginVM: LoginViewModel by viewModels()
    private lateinit var sharedPref: SharedPreferences

    override fun extraSetupBinding() {
        binding.apply {
            vm = loginVM
            lifecycleOwner = this@LoginActivity
        }
    }

    override fun setup() {
        // 거부 처리 해줘야 함...
        requirePermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERM_FINE_LOCATION)
    }

    override fun setupViews() {
        supportActionBar?.hide()
    }

    override fun onSubscribe() {
        binding.signInButton.setOnClickListener {
            startActivityForResult(getGoogleSignInIntent(this).signInIntent, RESULT_CODE)
        }
    }

    override fun release() { }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        Log.e("RESULT", result?.status.toString())

        if (resultCode == RESULT_OK && requestCode == RESULT_CODE) {
            val loginEmail = googleSignIn(data)
            if (loginVM.isEmailValid(loginEmail)) {
                // SharedPreferences에 이메일 저장
                PreferencesManager.setString(this, getString(R.string.saved_google_email), loginEmail)
                loginVM.login(loginEmail, { startMainActivity() }, { startSignUpActivity() })
            }
        }
    }

    override fun permissionGranted(requestCode: Int) {
        sharedPref = this?.getPreferences(MODE_PRIVATE) ?: return

        if (firebaseAuth.currentUser == null) {
            PreferencesManager.setString(this, getString(R.string.saved_google_email), "")
        } else {
            loginVM.login(firebaseAuth.currentUser.email, { startMainActivity() }, { Toast.makeText(this, "로그인이 불안정합니다.", Toast.LENGTH_LONG).show()})
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERM_COARSE_LOCATION -> {
                Toast.makeText(baseContext, "위치 권한을 승인해야 지도를 사용할 수 있습니다! [COARSE_LOCATION]", Toast.LENGTH_SHORT).show()
                finishAffinity()
            }
            PERM_FINE_LOCATION -> {
                Toast.makeText(baseContext, "위치 권한을 승인해야 지도를 사용할 수 있습니다! [FINE_LOCATION]", Toast.LENGTH_SHORT).show()
                finishAffinity()
            }
        }
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).let {
            startActivity(it)
            finish()
        }
    }

    private fun startSignUpActivity() {
        Intent(this, SignUpActivity::class.java).let {
            startActivity(it)
            finish()
        }
    }
}