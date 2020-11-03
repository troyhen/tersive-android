package com.troy.tersive.ui.flashcard

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Dimension.Companion.fillToConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.troy.tersive.R
import com.troy.tersive.model.db.user.entity.Learn
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.ui.base.AppTheme
import com.troy.tersive.ui.base.appBackground
import com.troy.tersive.ui.base.cardBackground
import com.troy.tersive.ui.base.colorPrimary
import com.troy.tersive.ui.nav.NavControllerAmbient
import org.koin.androidx.compose.getViewModel

//class FlashCardActivity : AppCompatActivity() {
//
//    @Inject
//    lateinit var tersiveUtil: TersiveUtil
//
//    private lateinit var arguments: FlashCardArguments
//    private lateinit var binding: ActivityFlashCardBinding
//    private val viewModel: FlashCardViewModel by viewModels()
//
//    private var keyTypeFace: Typeface? = null
//    private var learnTypeFace: Typeface? = null
//    private var tersiveTypeFace: Typeface? = null
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == SIGN_IN.ordinal) {
//            val response = IdpResponse.fromResultIntent(data)
//            if (resultCode == Activity.RESULT_OK) {
//                // Successfully signed in
//                FirebaseAuth.getInstance().currentUser?.let { user ->
//                    viewModel.onLogin(user)
//                }
//            } else {
//                when (response?.error?.errorCode) {
//                    null -> {
//                    }
//                    else -> Timber.e(response.error)
//                }
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            FlashCardPage()
//        }
//        binding = ActivityFlashCardBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        arguments = FlashCardArguments.fromIntent(intent)
//        viewModel.phraseType = arguments.phraseType
//        learnTypeFace = Typeface.DEFAULT
//        keyTypeFace = Typeface.MONOSPACE
//        tersiveTypeFace = Typeface.createFromAsset(assets, "tersive_script.otf")
//        initListeners()
//        viewModel.initObservers()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        reset()
//    }
//
//    private fun initListeners() = binding.apply {
//        quizCard.setOnClickListener { showAnswer() }
//        showButton.setOnClickListener { showAnswer() }
//        doneButton.setOnClickListener { finishAfterTransition() }
//        easyButton.setOnClickListener { viewModel.updateCard(EASY) }
//        goodButton.setOnClickListener { viewModel.updateCard(GOOD) }
//        hardButton.setOnClickListener { viewModel.updateCard(HARD) }
//        againButton.setOnClickListener { viewModel.updateCard(AGAIN) }
//    }
//
//    private fun showAnswer() = binding.apply {
//        if (answerCard.isInvisible) {
//            answerCard.isInvisible = false
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun FlashCardViewModel.initObservers() = binding.apply {
//        collectWhenStarted(cardFlow.filterNotNull()) { card ->
//            answerCard.isInvisible = true
//            val wordPhraseId = when {
//                card.learn.flags and Learn.PHRASE == 0 -> R.string.word
//                else -> R.string.phrase
//            }
//            wordPhraseText.setText(wordPhraseId)
//            val frontBackId = when {
//                card.front -> R.string.front
//                else -> R.string.back
//            }
//            frontBackText.setText(frontBackId)
//            val scriptKeyId = when {
//                card.learn.flags and Learn.KEY == 0 -> R.string.tersive_script
//                else -> R.string.tersive_key
//            }
//            scriptKeyText.setText(scriptKeyId)
//            val tersive =
//                if (typeMode) card.learn.tersive else tersiveUtil.optimizeHand(card.learn.tersive)
//            val phrases = card.tersiveList.asSequence()
//                .mapNotNull { it.phrase }
//                .joinToString(", ") { it }
//            if (card.front) {
//                quizText.run {
//                    text = phrases
//                    textSize = LEARN_SIZE
//                    typeface = learnTypeFace
//                }
//                quizPencilLine.isVisible = false
//                answerText.run {
//                    if (typeMode) {
//                        text = tersive
//                        textSize = KEY_SIZE
//                        typeface = keyTypeFace
//                        answerPencilLine.isVisible = false
//                    } else {
//                        text = tersive
//                        textSize = TERSIVE_SIZE
//                        typeface = tersiveTypeFace
//                        answerPencilLine.isVisible = true
//                    }
//                }
//            } else {
//                quizText.run {
//                    if (typeMode) {
//                        text = tersive
//                        textSize = KEY_SIZE
//                        typeface = keyTypeFace
//                        quizPencilLine.isVisible = false
//                    } else {
//                        text = tersive
//                        textSize = TERSIVE_SIZE
//                        typeface = tersiveTypeFace
//                        quizPencilLine.isVisible = true
//                    }
//                }
//                answerText.run {
//                    text = phrases
//                    textSize = LEARN_SIZE
//                    typeface = learnTypeFace
//                }
//                answerPencilLine.isVisible = false
//            }
//        }
//    }
//
//    private fun reset() {
//        if (viewModel.needLogin) {
//            LoginActivity.startForResult(this)
//        } else {
//            binding.answerCard.isInvisible = true
//            viewModel.nextCard()
//        }
//    }
//
//    companion object {
//
//        const val KEY_SIZE = 32f
//        const val LEARN_SIZE = 32f
//        const val TERSIVE_SIZE = 100f
//
//        fun start(context: Context, phraseType: FlashCardRepo.Type) {
//            val intent = FlashCardArguments(phraseType).toIntent(context)
//            context.startActivity(intent)
//        }
//    }
//}

@Composable
fun FlashCardPage(phraseType: FlashCardRepo.Type = FlashCardRepo.Type.any) {
    val viewModel: FlashCardViewModel = getViewModel()
    viewModel.autoSignIn()
    viewModel.phraseType = phraseType
    val isLoggedIn = viewModel.isLoggedInFlow.collectAsState(false)
    AppTheme {
        if (!isLoggedIn.value) return@AppTheme
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(appBackground)
        ) {
            QuestionCard()
            if (viewModel.showAnswer.value) {
                AnswerCard()
            } else {
                HideAnswer()
            }
        }
    }
}

@Composable
fun ColumnScope.HideAnswer() {
    Spacer(modifier = Modifier.weight(1f))
}

@Preview
@Composable
private fun FlashCardPreview() {
    FlashCardPage(FlashCardRepo.Type.word)
}

@Composable
private fun ColumnScope.QuestionCard() {
    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(8.dp, 8.dp, 8.dp, 4.dp)
            .clip(RoundedCornerShape(8.dp)),
        backgroundColor = cardBackground,
        elevation = 6.dp,
    ) {
        val viewModel: FlashCardViewModel = getViewModel()
        val scriptKeyId = when {
            viewModel.cardFlags and Learn.KEY == 0 -> R.string.tersive_script
            else -> R.string.tersive_key
        }
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (doneButton, scriptKeyText, wordPhraseText, frontBackText, quizPencilLine,
                quizText, showButton) = createRefs()
            val navController = NavControllerAmbient.current
            TextButton(onClick = { navController.popBackStack() }, modifier = Modifier.constrainAs(doneButton) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
            }) {
                Text(text = stringResource(id = R.string.end_session))
            }
            Text(
                text = stringResource(scriptKeyId),
                modifier = Modifier.constrainAs(scriptKeyText) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                },
                style = MaterialTheme.typography.caption,
            )
            val wordPhraseId = when {
                viewModel.cardFlags and Learn.PHRASE == 0 -> R.string.word
                else -> R.string.phrase
            }
            Text(
                text = stringResource(wordPhraseId),
                modifier = Modifier.constrainAs(wordPhraseText) {
                    top.linkTo(scriptKeyText.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                },
                style = MaterialTheme.typography.caption,
            )
            val frontBackId = when {
                viewModel.isFront -> R.string.front
                else -> R.string.back
            }
            Text(
                text = stringResource(frontBackId),
                modifier = Modifier.constrainAs(frontBackText) {
                    top.linkTo(wordPhraseText.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                },
                style = MaterialTheme.typography.caption,
            )
            if (!viewModel.isFront && !viewModel.typeMode) {
                Box(Modifier
                    .constrainAs(quizPencilLine) {
                        linkTo(quizText.top, quizText.bottom, 1.dp)
                        start.linkTo(quizText.start)
                        end.linkTo(quizText.end)
                        width = fillToConstraints
                    }
                    .preferredHeight(1.dp)
                    .background(colorPrimary)
                )
            }
            Text(
                text = viewModel.cardQuestion,
                modifier = Modifier
                    .constrainAs(quizText) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    }
                    .padding(16.dp),
                fontFamily = viewModel.questionFont,
                style = viewModel.questionStyle(),
            )
            if (!viewModel.showAnswer.value) {
                TextButton(onClick = {
                    viewModel.showAnswer.value = true
                }, Modifier.constrainAs(showButton) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    centerHorizontallyTo(parent)
                }) {
                    Text(text = stringResource(R.string.show_answer))
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.AnswerCard() {
    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(8.dp, 4.dp, 8.dp, 8.dp)
            .clip(RoundedCornerShape(8.dp)),
        backgroundColor = cardBackground,
        elevation = 6.dp,
    ) {
        val viewModel: FlashCardViewModel = getViewModel()
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (answerText, answerPencilLine, easyButton, goodButton, hardButton, againButton) = createRefs()
            if (viewModel.isFront && !viewModel.typeMode) {
                Box(Modifier
                    .constrainAs(answerPencilLine) {
                        linkTo(answerText.top, answerText.bottom, 1.dp)
                        start.linkTo(answerText.start)
                        end.linkTo(answerText.end)
                        width = fillToConstraints
                    }
                    .preferredHeight(1.dp)
                    .background(colorPrimary)
                )
            }
            Text(
                text = viewModel.cardAnswer,
                modifier = Modifier
                    .constrainAs(answerText) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    }
                    .padding(16.dp),
                fontFamily = viewModel.answerFont,
                style = viewModel.answerStyle(),
            )
            TextButton(onClick = {
                viewModel.updateCard(FlashCardRepo.Result.EASY)
            }, Modifier.constrainAs(easyButton) {
                start.linkTo(parent.start)
                end.linkTo(goodButton.start)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            }) {
                Text(text = stringResource(R.string.easy))
            }
            TextButton(onClick = {
                viewModel.updateCard(FlashCardRepo.Result.GOOD)
            }, Modifier.constrainAs(goodButton) {
                start.linkTo(easyButton.end)
                end.linkTo(hardButton.start)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            }) {
                Text(text = stringResource(R.string.good))
            }
            TextButton(onClick = {
                viewModel.updateCard(FlashCardRepo.Result.HARD)
            }, Modifier.constrainAs(hardButton) {
                start.linkTo(goodButton.end)
                end.linkTo(againButton.start)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            }) {
                Text(text = stringResource(R.string.hard))
            }
            TextButton(onClick = {
                viewModel.updateCard(FlashCardRepo.Result.AGAIN)
            }, Modifier.constrainAs(againButton) {
                start.linkTo(hardButton.end)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            }) {
                Text(text = stringResource(R.string.again))
            }
        }
    }
}
