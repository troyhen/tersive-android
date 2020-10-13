package com.troy.tersive.ui.main

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.troy.tersive.R
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.ui.base.AppTheme
import com.troy.tersive.ui.base.drawerCenterColor
import com.troy.tersive.ui.base.drawerEndColor
import com.troy.tersive.ui.base.drawerStartColor
import com.troy.tersive.ui.base.selectedColor
import com.troy.tersive.ui.nav.MainScreen
import com.troy.tersive.ui.read.ReadListActivity

//@AndroidEntryPoint
//class MainActivity : AppCompatActivity() {//}, NavigationView.OnNavigationItemSelectedListener {

//    private val viewModel: MainViewModel by viewModels()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            AppTheme {
//                MainPage()
//            }
//        }
//        setSupportActionBar(binding.toolbar)
//
//        val toggle = ActionBarDrawerToggle(
//            this,
//            binding.drawer_layout,
//            toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//        binding.drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        val nav: NavigationView = findViewById(R.id.nav_view)
//        nav.setNavigationItemSelectedListener(this)
//        val typeMode = viewModel.typeMode
//        binding.hand_mode.isSelected = !typeMode
//        binding.type_mode.isSelected = typeMode
//        setupListeners()
//    }
//
//    override fun onBackPressed() {
//        if (binding.drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            binding.drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.nav_camera -> {
//                // Handle the camera action
//            }
//            R.id.nav_gallery -> {
//
//            }
//            R.id.nav_slideshow -> {
//
//            }
//            R.id.nav_manage -> {
//
//            }
//            R.id.nav_share -> {
//
//            }
//            R.id.nav_send -> {
//
//            }
//        }
//
//        binding.drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }
//
//    private fun setupListeners() = binding.apply {
//        hand_mode.setOnClickListener {
//            hand_mode.isSelected = true
//            type_mode.isSelected = false
//            viewModel.typeMode = false
//        }
//        type_mode.setOnClickListener {
//            hand_mode.isSelected = false
//            type_mode.isSelected = true
//            viewModel.typeMode = true
//        }
//        tersiveIntroButton.setOnClickListener {
//            IntroActivity.start(this)
//        }
//        wordFlashCardButton.setOnClickListener {
//            FlashCardActivity.start(this, FlashCardRepo.Type.WORD_ONLY)
//        }
//        phraseFlashCardButton.setOnClickListener {
//            FlashCardActivity.start(this, FlashCardRepo.Type.PHRASE_ONLY)
//        }
//        readingButton.setOnClickListener {
//            ReadListActivity.start(this)
//        }
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//    }
//}

@Composable
fun MainPage() {
    val viewModel: MainViewModel = viewModel()
    AppTheme {
        Scaffold(topBar = {
            AppBar()
        }, drawerContent = {
            NavDrawer()
        }) {
            MainButtons(viewModel.typeMode) {
                viewModel.typeMode = it
            }
        }
    }
}

@Composable
private fun AppBar() {
//    val navController = NavControllerAmbient.current
    TopAppBar(
//        navigationIcon = {
//            IconButton(onClick = { /*navController.popBackStack()*/ }) {
//                Icon(Icons.Filled.ArrowBack)
//            }
//        },
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = {
                MainScreen.goAdminMenu()
            }) {
                Icon(Icons.Filled.Settings)
            }
//            IconButton(onClick = { viewModel.confirmDeleteState = true }) {
//                Icon(Icons.Filled.Delete)
//            }
        },
    )
}

@Composable
private fun NavDrawer() {
    NavDrawerHeader()
}

@Composable
private fun NavDrawerHeader() {
    Column(
        modifier = Modifier
            .background(LinearGradient(colors = listOf(drawerStartColor, drawerCenterColor, drawerEndColor), startX = 0f, startY = 0f, endX = 200f, endY = 500f))
            .padding(dimensionResource(R.dimen.activity_horizontal_margin), dimensionResource(R.dimen.activity_vertical_margin))
    ) {
//        Image(asset = imageResource(R.mipmap.ic_launcher_round), modifier = Modifier.padding(top = dimensionResource(R.dimen.nav_header_vertical_spacing)))
        Text(text = stringResource(R.string.nav_header_title), modifier = Modifier.padding(top = dimensionResource(R.dimen.nav_header_vertical_spacing)), style = MaterialTheme.typography.body1)
        Text(text = stringResource(R.string.nav_header_subtitle), style = MaterialTheme.typography.body1)
    }
}

@Composable
private fun MainButtons(typeMode: Boolean = false, setMode: (Boolean) -> Unit = {}) {
//    val viewModel: MainViewModel = viewModel()
    val context = ContextAmbient.current
    val padding = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)
    val paddingExtra = Modifier.padding(start = 8.dp, top = 24.dp, end = 8.dp)
    Column {
        Row(paddingExtra) {
            TersiveButton(modifier = Modifier.weight(1f), image = imageResource(R.drawable.hand_mode), text = stringResource(R.string.hand), selected = !typeMode) {
                setMode(false)
            }
            Spacer(modifier = Modifier.width(8.dp))
            TersiveButton(modifier = Modifier.weight(1f), image = imageResource(R.drawable.type_mode), text = stringResource(R.string.type), selected = typeMode) {
                setMode(true)
            }
        }
        TersiveButton(modifier = paddingExtra, image = imageResource(R.drawable.handwriting), text = stringResource(R.string.tersive_basics)) {
            MainScreen.goIntro()
        }
        TersiveButton(modifier = paddingExtra, image = imageResource(R.drawable.word_cards), text = stringResource(R.string.word_cards)) {
            MainScreen.goFlashCards(FlashCardRepo.Type.WORD_ONLY)
        }
        TersiveButton(modifier = padding, image = imageResource(R.drawable.phrase_cards), text = stringResource(R.string.phrase_cards)) {
            MainScreen.goFlashCards(FlashCardRepo.Type.PHRASE_ONLY)
        }
        TersiveButton(modifier = paddingExtra, image = imageResource(R.drawable.practice_reading), text = stringResource(R.string.practice_reading)) {
            ReadListActivity.start(context)
        }
        TersiveButton(modifier = padding, image = imageResource(R.drawable.practice_writing), text = stringResource(R.string.practice_writing))
    }
}

@Composable
@Preview
private fun MainPreview() {
    MainButtons()
}

@Composable
private fun TersiveButton(
    modifier: Modifier = Modifier,
    image: ImageAsset,
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val background = if (selected) selectedColor else MaterialTheme.colors.surface
    val textColor = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
    Box(
        modifier
            .clickable(onClick = onClick)
            .background(background)
    ) {
        Image(
            asset = image,
            modifier = Modifier.fillMaxWidth().preferredHeight(40.dp),
            contentScale = ContentScale.Crop,
            alpha = .2f
        )
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            color = textColor
        )
    }
}