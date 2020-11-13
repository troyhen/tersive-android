package com.troy.tersive.ui.flashcard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Dimension.Companion.fillToConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.asFontFamily
import androidx.compose.ui.text.font.font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.troy.tersive.R
import com.troy.tersive.model.data.Card
import com.troy.tersive.model.data.CardResult
import com.troy.tersive.model.data.CardType
import com.troy.tersive.model.db.tersive.Tersive
import com.troy.tersive.model.db.user.entity.Learn
import com.troy.tersive.ui.base.AppTheme
import com.troy.tersive.ui.base.appBackground
import com.troy.tersive.ui.base.cardBackground
import com.troy.tersive.ui.base.colorPrimary
import com.troy.tersive.ui.nav.NavControllerAmbient

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
fun FlashCardPage(cardType: CardType = CardType.ANY) {
//todo broken in Koin 2.2.0    val viewModel: FlashCardViewModel = getViewModel()
    val viewModel: FlashCardViewModel = viewModel(factory = FlashCardViewModelFactory)
    viewModel.cardType = cardType
    viewModel.autoSignIn()
    val cardState = viewModel.cardFlow.collectAsState(initial = null)
    val card = cardState.value ?: return
    AppTheme {
        when (ConfigurationAmbient.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> LandscapePage(viewModel = viewModel, card = card)
            else -> PortraitPage(viewModel = viewModel, card = card)
        }
    }
}

@Composable
fun LandscapePage(viewModel: FlashCardViewModel, card: Card) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackground)
    ) {
        val weight = Modifier.weight(1f)
        QuestionCard(weight, viewModel, card)
        if (viewModel.showAnswer.value) {
            AnswerCard(weight, viewModel, card)
        } else {
            HideAnswer(weight)
        }
    }
}

@Composable
fun PortraitPage(viewModel: FlashCardViewModel, card: Card) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackground)
    ) {
        val weight = Modifier.weight(1f)
        QuestionCard(weight, viewModel, card)
        if (viewModel.showAnswer.value) {
            AnswerCard(weight, viewModel, card)
        } else {
            HideAnswer(weight)
        }
    }
}

@Composable
private fun QuestionCard(modifier: Modifier, viewModel: FlashCardViewModel? = null, card: Card = previewCard) {
    val cardFlags = card.learn.flags
    val isFront = card.front
    val typeMode = viewModel?.typeMode ?: false
    val cardQuestion = viewModel?.cardQuestion(card, typeMode) ?: "it, to"
    val questionFont = viewModel?.questionFont(card) ?: FontFamily.Default
    val questionStyle = viewModel?.questionStyle(card) ?: MaterialTheme.typography.h6
    val showAnswer = viewModel?.showAnswer?.value ?: false
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp, 8.dp, 8.dp, 4.dp)
            .clip(RoundedCornerShape(8.dp)),
        backgroundColor = cardBackground,
        elevation = 6.dp,
    ) {
        val scriptKeyId = when {
            cardFlags and Learn.KEY == 0 -> R.string.tersive_script
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
                cardFlags and Learn.PHRASE == 0 -> R.string.word
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
                isFront -> R.string.front
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
            if (!isFront && !typeMode) {
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
                text = cardQuestion,
                modifier = Modifier
                    .constrainAs(quizText) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    }
                    .padding(16.dp),
                fontFamily = questionFont,
                style = questionStyle,
            )
            if (!showAnswer) {
                TextButton(onClick = {
                    viewModel?.showAnswer?.value = true
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
fun HideAnswer(modifier: Modifier) {
    Spacer(modifier = modifier)
}

@Composable
private fun AnswerCard(modifier: Modifier, viewModel: FlashCardViewModel? = null, card: Card = previewCard) {
    val isFront = card.front
    val typeMode = viewModel?.typeMode ?: false
    val cardAnswer = viewModel?.cardAnswer(card, typeMode) ?: "t"
    val answerFont = viewModel?.answerFont(card) ?: font(R.font.tersive_script).asFontFamily()
    val answerStyle = viewModel?.answerStyle(card) ?: MaterialTheme.typography.h1
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp, 8.dp, 8.dp)
            .clip(RoundedCornerShape(8.dp)),
        backgroundColor = cardBackground,
        elevation = 6.dp,
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (answerText, answerPencilLine, easyButton, goodButton, hardButton, againButton) = createRefs()
            if (isFront && !typeMode) {
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
                text = cardAnswer,
                modifier = Modifier
                    .constrainAs(answerText) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    }
                    .padding(16.dp),
                fontFamily = answerFont,
                style = answerStyle,
            )
            TextButton(onClick = {
                viewModel?.updateCard(card, CardResult.EASY)
            }, Modifier.constrainAs(easyButton) {
                start.linkTo(parent.start)
                end.linkTo(goodButton.start)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            }) {
                Text(text = stringResource(R.string.easy))
            }
            TextButton(onClick = {
                viewModel?.updateCard(card, CardResult.GOOD)
            }, Modifier.constrainAs(goodButton) {
                start.linkTo(easyButton.end)
                end.linkTo(hardButton.start)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            }) {
                Text(text = stringResource(R.string.good))
            }
            TextButton(onClick = {
                viewModel?.updateCard(card, CardResult.HARD)
            }, Modifier.constrainAs(hardButton) {
                start.linkTo(goodButton.end)
                end.linkTo(againButton.start)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            }) {
                Text(text = stringResource(R.string.hard))
            }
            TextButton(onClick = {
                viewModel?.updateCard(card, CardResult.AGAIN)
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

private val previewCard = Card(true, 0, Learn(0, "", 0, "t", 0), listOf(Tersive(0L, "it", "it", "it", "it", "t", "t", 1, 0, null, 0)))

@Preview
@Composable
private fun questionPreview() {
    Column(
        modifier = Modifier
            .height(300.dp)
            .background(appBackground)
    ) {
        QuestionCard(Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun answerPreview() {
    Column(
        modifier = Modifier
            .height(300.dp)
            .background(appBackground)
    ) {
        AnswerCard(Modifier.weight(1f))
    }
}
