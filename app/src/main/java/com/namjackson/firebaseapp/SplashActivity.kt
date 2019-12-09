package com.namjackson.firebaseapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseAuth.getInstance().signOut()
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        currentUser?.delete()
        if (currentUser == null) {
            startFireBaseSingin()
        } else {
            startMain()
        }
    }

    fun startFireBaseSingin() {

        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.AppTheme)
                .setLogo(R.mipmap.ic_launcher)
                .setAvailableProviders(providers)
                .build(),
            REQ_CODE_FIREBASE_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_CODE_FIREBASE_SIGN_IN -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        startMain()
                    }
                }
            }
        }
    }

    fun startMain() {
        MainActivity.startActivity(this)
    }

    companion object {
        const val REQ_CODE_FIREBASE_SIGN_IN = 61597
    }
}
