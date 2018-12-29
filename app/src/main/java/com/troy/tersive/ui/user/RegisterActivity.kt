package com.troy.tersive.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.troy.tersive.R
import com.troy.tersive.app.Injector
import kotlinx.android.synthetic.main.activity_register.*
import org.lds.mobile.livedata.observeNotNull
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        refreshLayout.isEnabled = false
        initListeners()
        viewModel.initObservers()
    }

    private fun initListeners() {
        registerButton.setOnClickListener {
            it.isVisible = false
            refreshLayout.isRefreshing = true
            viewModel.register(
                emailAddress.text?.toString(),
                password1.text?.toString(),
                password2.text?.toString()
            )
        }
    }

    private fun RegisterViewModel.initObservers() {
        registerEvent.observeNotNull(this@RegisterActivity) {
            registerButton.isVisible = true
            refreshLayout.isRefreshing = false
            when (it) {
                RegisterViewModel.RegisterResult.SUCCESS -> {
                    Toast.makeText(this@RegisterActivity, R.string.login_success, Toast.LENGTH_LONG)
                        .show()
                    finish()
                }
                RegisterViewModel.RegisterResult.DUPLICATE_USERNAME -> Toast.makeText(
                    this@RegisterActivity,
                    R.string.registration_taken,
                    Toast.LENGTH_LONG
                ).show()
                RegisterViewModel.RegisterResult.PASSWORD_MISMATCH -> Toast.makeText(
                    this@RegisterActivity,
                    R.string.password_mismatch,
                    Toast.LENGTH_LONG
                ).show()
                RegisterViewModel.RegisterResult.MISSING_FIELD -> Toast.makeText(
                    this@RegisterActivity,
                    R.string.missing_field,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}
