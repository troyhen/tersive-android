package com.troy.tersive.ui.user

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.troy.tersive.R
import com.troy.tersive.app.Injector
import com.troy.tersive.ui.base.RequestCode.SIGN_IN
import kotlinx.android.synthetic.main.activity_login.*
import org.lds.mobile.livedata.observeNotNull
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initListeners()
        viewModel.initObservers()
    }

    private fun initListeners() {
        loginButton.setOnClickListener {
            viewModel.login(
                emailAddress.text.toString(),
                password.text.toString()
            )
        }
        registerButton.setOnClickListener {
            RegisterActivity.start(this)
            finish()
        }
    }

    private fun LoginViewModel.initObservers() {
        loginEvent.observeNotNull(this@LoginActivity) {
            if (it) {
                Toast.makeText(this@LoginActivity, R.string.login_success, Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this@LoginActivity, R.string.login_failure, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        fun startForResult(activity: Activity) {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()/*,
                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.TwitterBuilder().build()*/
            )

            activity.startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                SIGN_IN.ordinal
            )
        }
    }
}
