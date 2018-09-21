package com.troy.tersive.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.troy.tersive.R
import com.troy.tersive.app.Injector
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
        registerButton.setOnClickListener { RegisterActivity.start(this) }
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
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}
