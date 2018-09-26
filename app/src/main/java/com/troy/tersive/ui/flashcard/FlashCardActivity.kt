package com.troy.tersive.ui.flashcard

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.troy.tersive.R
import com.troy.tersive.app.Injector
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.ui.user.LoginActivity
import kotlinx.android.synthetic.main.activity_flash_card.*
import org.lds.mobile.livedata.observeNotNull
import org.lds.mobile.ui.ext.isInvisible
import javax.inject.Inject

class FlashCardActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: Prefs
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FlashCardViewModel::class.java)
    }

    init {
        Injector.get().inject(this)
    }

    private var learnTypeFace: Typeface? = null
    private var tersiveTypeFace: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_card)
        intent.extras?.getInt(EXTRA_PHRASE_MODE)?.let {
            viewModel.phraseMode = FlashCardRepo.Type.values()[it]
        }
        learnTypeFace = Typeface.DEFAULT
        tersiveTypeFace = Typeface.createFromAsset(assets, "Tersive_Script.otf")
        quizCard.setOnClickListener {
            if (answerCard.isInvisible) {
                answerCard.isInvisible = false
            }
        }
        viewModel.initObservers()
    }

    override fun onStart() {
        super.onStart()
        reset()
    }

    private fun FlashCardViewModel.initObservers() {
        val keyMode = prefs.keyMode
        cardLiveData.observeNotNull(this@FlashCardActivity) { card ->
            val tersive = if (keyMode) card.learn.kbd else card.learn.lvl4
            val phrases = card.tersiveList.asSequence()
                .mapNotNull { it.phrase }
                .joinToString(", ") { it }
            if (card.front) {
                quizText.run {
                    text = tersive
                    textSize = TERSIVE_SIZE
                    typeface = tersiveTypeFace
                }
                answerText.run {
                    text = phrases
                    textSize = LEARN_SIZE
                    typeface = learnTypeFace
                }
            } else {
                quizText.run {
                    text = phrases
                    textSize = LEARN_SIZE
                    typeface = learnTypeFace
                }
                answerText.run {
                    text = tersive
                    textSize = TERSIVE_SIZE
                    typeface = tersiveTypeFace
                }
            }
        }
    }

    private fun reset() {
        if (viewModel.needLogin) {
            LoginActivity.start(this)
        } else {
            answerCard.isInvisible = true
            viewModel.nextCard()
        }

    }

    companion object {

        const val EXTRA_PHRASE_MODE = "phraseMode"
        const val LEARN_SIZE = 32f
        const val TERSIVE_SIZE = 100f

        fun start(context: Context, phraseMode: FlashCardRepo.Type) {
            val intent = Intent(context, FlashCardActivity::class.java).apply {
                extras?.putInt(EXTRA_PHRASE_MODE, phraseMode.ordinal)
            }
            context.startActivity(intent)
        }
    }
}
