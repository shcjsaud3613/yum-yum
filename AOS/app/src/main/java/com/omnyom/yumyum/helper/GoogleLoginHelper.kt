package com.omnyom.yumyum.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.omnyom.yumyum.R
import com.omnyom.yumyum.helper.PreferencesManager.Companion.getString

class GoogleLoginHelper {
    companion object {
        const val RESULT_CODE = 10

        val firebaseAuth: FirebaseAuth by lazy {
            FirebaseAuth.getInstance()
        }

        fun getCurrentUserEmail(context: Context) : String? {
            var googleEmail = getString(context, context.getString(R.string.saved_google_email))
            firebaseAuth.currentUser?.let {
                googleEmail = it.email
            }
            return googleEmail
        }

        fun getGoogleSignInIntent(activity: Activity): GoogleSignInClient {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id)).requestEmail().build()

            return GoogleSignIn.getClient(activity, gso)
        }

        private fun getGoogleSignInResult(data: Intent?): GoogleSignInResult? {
            return Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        }

        fun googleSignIn(data: Intent?): String {
            var loginEmail = ""
            getGoogleSignInResult(data)?.let {
                if (it.isSuccess) {
                    firebaseLogin(it.signInAccount!!)
                    loginEmail = it.signInAccount?.email?: ""
                } else  {
                    Log.e("Value", "error")
                }
            }
            return loginEmail
        }

        private fun firebaseLogin(googleAccount: GoogleSignInAccount) {
            val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)

            firebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.user?.displayName //사용자 이름
                } else {
                    //error 처리
                }
            }?.addOnFailureListener {
                //error 처리
            }
        }

        fun googleSignOut(activity:Activity, logoutAction: (() -> Unit)? = null) {
            getGoogleSignInIntent(activity).signOut().addOnCompleteListener{
                logoutAction?.invoke()
            }
        }
    }
}