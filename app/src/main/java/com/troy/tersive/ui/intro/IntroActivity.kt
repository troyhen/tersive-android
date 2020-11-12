package com.troy.tersive.ui.intro

import android.print.PrintDocumentAdapter
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.viewModel
import com.troy.tersive.R
import com.troy.tersive.ui.base.AppTheme
import com.troy.tersive.ui.nav.NavControllerAmbient
import timber.log.Timber

//class IntroActivity : AppCompatActivity() {
//
//    private val viewModel: IntroViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//           AppTheme {
//                IntroPage()
//            }
//        }
//        setupReceiver()
//    }
//
//    override fun onBackPressed() {
//        viewModel.onBackPressed()
//    }
//
//    private fun setupReceiver() {
//        receiveWhenStarted(viewModel.eventChannel) { action ->
//            when (action) {
//                Finish -> finish()
//            }
//        }
//    }
//
//    companion object {
//        fun start(context: Context) {
//            context.startActivity(Intent(context, IntroActivity::class.java))
//        }
//    }
//}

@Composable
fun IntroPage() {
//todo broken in Koin 2.2.0    val viewModel: IntroViewModel = getViewModel()
    val viewModel: IntroViewModel = viewModel()
    AppTheme {
        Scaffold(topBar = { AppBar(viewModel) }) {
            WebComponent(url = "file:///android_asset/Tersive_Intro.html", webContext = viewModel.webContext)
        }
    }
}

@Composable
private fun AppBar(viewModel: IntroViewModel) {
    val navController = NavControllerAmbient.current
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { viewModel.onBackPressed(navController) }) {
                Icon(Icons.Filled.ArrowBack)
            }
        },
        title = {
            Text(stringResource(id = R.string.intro_title))
        },
//        actions = {
//            IconButton(onClick = {
////                val directions = IndividualFragmentDirections.actionIndividualEditFragment(viewModel.individualId)
////                navController.navigate(directions)
//            }) {
//                Icon(Icons.Filled.Edit)
//            }
//            IconButton(onClick = { viewModel.confirmDeleteState = true }) {
//                Icon(Icons.Filled.Delete)
//            }
//        },
    )
}

class WebContext {
    lateinit var webView: WebView

    fun createPrintDocumentAdapter(documentName: String): PrintDocumentAdapter {
        validateWebView()
        return webView.createPrintDocumentAdapter(documentName)
    }

    fun goForward() {
        validateWebView()
        webView.goForward()
    }

    fun goBack() {
        validateWebView()
        webView.goBack()
    }

    fun canGoBack(): Boolean {
        validateWebView()
        return webView.canGoBack()
    }

    private fun validateWebView() {
        if (!::webView.isInitialized) {
            throw IllegalStateException("The WebView is not initialized yet.")
        }
    }

    companion object {
        val debug = true
    }
}

private fun WebView.setRef(ref: (WebView) -> Unit) {
    ref(this)
}

private fun WebView.setUrl(url: String) {
    if (originalUrl != url) {
        if (WebContext.debug) {
            Timber.d("WebComponent load url")
        }
        loadUrl(url)
    }
}

@Composable
fun WebComponent(
    url: String,
    webViewClient: WebViewClient = WebViewClient(),
    webContext: WebContext
) {
    if (WebContext.debug) {
        Timber.d("WebComponent compose $url")
    }
    AndroidView(::WebView) {
        it.setRef { view -> webContext.webView = view }
        it.setUrl(url)
        it.webViewClient = webViewClient
    }
}