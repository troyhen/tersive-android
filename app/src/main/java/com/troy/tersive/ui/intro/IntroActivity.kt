package com.troy.tersive.ui.intro

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.troy.tersive.R
import com.troy.tersive.app.Injector
import kotlinx.android.synthetic.main.activity_intro.*
import org.lds.mobile.extras.SelfActivityCompanion
import javax.inject.Inject

class IntroActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(IntroViewModel::class.java)
    }

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        webView.loadUrl("file:///android_asset/Tersive_Intro.html")
    }

    companion object : SelfActivityCompanion<Companion>(IntroActivity::class) {
        fun startActivity(context: Context) {
            IntroActivity.start(context) { intent ->
            }
        }
    }
}
