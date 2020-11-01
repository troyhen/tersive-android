package com.troy.tersive.ui.nav

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.troy.tersive.model.repo.UserRepo
import com.troy.tersive.ui.base.RequestCode
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class NavActivity : AppCompatActivity() {

    private val userRepo: UserRepo by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavPage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestCode.SIGN_IN.ordinal) lifecycleScope.launch {
            if (resultCode == RESULT_OK) FirebaseAuth.getInstance().currentUser?.let { userRepo.login(it) }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun signIn() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build()/*,
                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.TwitterBuilder().build()*/
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RequestCode.SIGN_IN.ordinal
        )
    }
}
