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
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.model.repo.FlashCardRepo.Result.AGAIN
import com.troy.tersive.model.repo.FlashCardRepo.Result.EASY
import com.troy.tersive.model.repo.FlashCardRepo.Result.GOOD
import com.troy.tersive.model.repo.FlashCardRepo.Result.HARD
import com.troy.tersive.ui.user.LoginActivity
import kotlinx.android.synthetic.main.activity_flash_card.*
import org.lds.mobile.livedata.observeNotNull
import org.lds.mobile.ui.ext.isInvisible
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

    private var learnTypeFace: Typeface? = null
    private var tersiveTypeFace: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_card)
        intent.extras?.getInt(EXTRA_PHRASE_TYPE)?.let {
            viewModel.phraseType = FlashCardRepo.Type.values()[it]
        }
        learnTypeFace = Typeface.DEFAULT
        tersiveTypeFace = Typeface.createFromAsset(assets, "Tersive_Script.otf")
        initListeners()
        viewModel.initObservers()
    }

    override fun onStart() {
        super.onStart()
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
            val tersive =
                if (typeMode) card.learn.kbd else tersiveUtil.optimizeHand(card.learn.lvl4)
            val phrases = card.tersiveList.asSequence()
                .mapNotNull { it.phrase }
                .joinToString(", ") { it }
            if (card.front) {
                quizText.run {
                    text = tersive
                    textSize = TERSIVE_SIZE
                    typeface = tersiveTypeFace
//                    paintFlags = paintFlags or STRIKE_THRU_TEXT_FLAG
                }
                answerText.run {
                    text = phrases
                    textSize = LEARN_SIZE
                    typeface = learnTypeFace
//                    paintFlags = paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
                }
            } else {
                quizText.run {
                    text = phrases
                    textSize = LEARN_SIZE
                    typeface = learnTypeFace
//                    paintFlags = paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
                }
                answerText.run {
                    text = tersive
                    textSize = TERSIVE_SIZE
                    typeface = tersiveTypeFace
//                    paintFlags = paintFlags or STRIKE_THRU_TEXT_FLAG
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
        const val EXTRA_PHRASE_TYPE = "phraseType"
        const val LEARN_SIZE = 32f
        const val TERSIVE_SIZE = 100f

        fun start(context: Context, phraseType: FlashCardRepo.Type) {
            val intent = Intent(context, FlashCardActivity::class.java).apply {
                extras?.putInt(EXTRA_PHRASE_TYPE, phraseType.ordinal)
            }
            context.startActivity(intent)
        }
    }
}
