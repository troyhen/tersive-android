package com.troy.tersive.ui.flashcard

import android.annotation.SuppressLint
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
import com.troy.tersive.model.data.TersiveUtil
import com.troy.tersive.model.db.user.entity.Learn
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.model.repo.FlashCardRepo.Result.AGAIN
import com.troy.tersive.model.repo.FlashCardRepo.Result.EASY
import com.troy.tersive.model.repo.FlashCardRepo.Result.GOOD
import com.troy.tersive.model.repo.FlashCardRepo.Result.HARD
import com.troy.tersive.ui.user.LoginActivity
import kotlinx.android.synthetic.main.activity_flash_card.*
import me.eugeniomarletti.extras.intent.IntentExtra
import me.eugeniomarletti.extras.intent.base.Int
import org.lds.mobile.extras.SelfActivityCompanion
import org.lds.mobile.livedata.observeNotNull
import org.lds.mobile.ui.ext.isInvisible
import org.lds.mobile.ui.ext.isVisible
import javax.inject.Inject

class FlashCardActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: Prefs
    @Inject
    lateinit var tersiveUtil: TersiveUtil
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FlashCardViewModel::class.java)
    }

    init {
        Injector.get().inject(this)
    }

    private var keyTypeFace: Typeface? = null
    private var learnTypeFace: Typeface? = null
    private var tersiveTypeFace: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_card)
        intent.phraseTypeExtra?.let {
            viewModel.phraseType = FlashCardRepo.Type.values()[it]
        }
        learnTypeFace = Typeface.DEFAULT
        keyTypeFace = Typeface.MONOSPACE
        tersiveTypeFace = Typeface.createFromAsset(assets, "Tersive_Script.otf")
        initListeners()
        viewModel.initObservers()
        reset()
    }

    private fun initListeners() {
        quizCard.setOnClickListener { showAnswer() }
        showButton.setOnClickListener { showAnswer() }
        doneButton.setOnClickListener { finishAfterTransition() }
        easyButton.setOnClickListener { viewModel.updateCard(EASY) }
        goodButton.setOnClickListener { viewModel.updateCard(GOOD) }
        hardButton.setOnClickListener { viewModel.updateCard(HARD) }
        againButton.setOnClickListener { viewModel.updateCard(AGAIN) }
    }

    private fun showAnswer() {
        if (answerCard.isInvisible) {
            answerCard.isInvisible = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun FlashCardViewModel.initObservers() {
        cardLiveData.observeNotNull(this@FlashCardActivity) { card ->
            answerCard.isInvisible = true
            val wordPhraseId = when {
                card.learn.flags and Learn.WORD == 0 -> R.string.word
                else -> R.string.phrase
            }
            wordPhraseText.setText(wordPhraseId)
            val frontBackId = when {
                card.front -> R.string.front
                else -> R.string.back
            }
            frontBackText.setText(frontBackId)
            val scriptKeyId = when {
                card.learn.flags and Learn.KEY == 0 -> R.string.tersive_script
                else -> R.string.tersive_key
            }
            scriptKeyText.setText(scriptKeyId)
            val tersive =
                if (typeMode) card.learn.tersive else tersiveUtil.optimizeHand(card.learn.tersive)
            val phrases = card.tersiveList.asSequence()
                .mapNotNull { it.phrase }
                .joinToString(", ") { it }
            if (card.front) {
                quizText.run {
                    text = phrases
                    textSize = LEARN_SIZE
                    typeface = learnTypeFace
                }
                quizPencilLine.isVisible = false
                answerText.run {
                    if (typeMode) {
                        text = tersive
                        textSize = KEY_SIZE
                        typeface = keyTypeFace
                        answerPencilLine.isVisible = false
                    } else {
                        text = tersive
                        textSize = TERSIVE_SIZE
                        typeface = tersiveTypeFace
                        answerPencilLine.isVisible = true
                    }
                }
            } else {
                quizText.run {
                    if (typeMode) {
                        text = tersive
                        textSize = KEY_SIZE
                        typeface = keyTypeFace
                        quizPencilLine.isVisible = false
                    } else {
                        text = tersive
                        textSize = TERSIVE_SIZE
                        typeface = tersiveTypeFace
                        quizPencilLine.isVisible = true
                    }
                }
                answerText.run {
                    text = phrases
                    textSize = LEARN_SIZE
                    typeface = learnTypeFace
                }
                answerPencilLine.isVisible = false
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

    companion object : SelfActivityCompanion<Companion>(FlashCardActivity::class) {

        const val KEY_SIZE = 32f
        const val LEARN_SIZE = 32f
        const val TERSIVE_SIZE = 100f

        var Intent.phraseTypeExtra by IntentExtra.Int()

        fun start(context: Context, phraseType: FlashCardRepo.Type) {
            FlashCardActivity.start(context) { intent ->
                intent.phraseTypeExtra = phraseType.ordinal
            }
        }
    }
}
