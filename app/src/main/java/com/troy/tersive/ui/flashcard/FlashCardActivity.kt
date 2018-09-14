package com.troy.tersive.ui.flashcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.troy.tersive.app.Injector
import javax.inject.Inject

class FlashCardActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FlashCardViewModel::class.java)
    }

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FlashCardActivity::class.java)
            context.startActivity(intent)
        }
    }
}
