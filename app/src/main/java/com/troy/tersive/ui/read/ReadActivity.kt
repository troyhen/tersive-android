package com.troy.tersive.ui.read

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.troy.tersive.R
import com.troy.tersive.model.data.WebDoc
import com.troy.tersive.model.ext.collectWhenStarted
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ReadActivity : AppCompatActivity() {

    private lateinit var arguments: ReadActivityArguments
    private val viewModel: ReadViewModel = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        arguments = ReadActivityArguments.fromIntent(intent)!!
//        toolbar.setup()
        viewModel.observe()
    }

    private fun ReadViewModel.observe() {
        textFlow.value = getString(R.string.loading)
        webDoc = arguments.webDoc
        collectWhenStarted(textFlow) {
//            textView.text = it
        }
    }

    private fun Toolbar.setup() {
        title = arguments.webDoc.title
//        navigationIcon = tintDrawable(R.drawable.ic_lds_arrow_back_24dp, R.color.white)
        setNavigationOnClickListener { finish() }
    }

    companion object {
        fun startActivity(context: Context, webDoc: WebDoc) {
            val arguments = ReadActivityArguments(webDoc)
            val intent = arguments.toIntent(context)
            context.startActivity(intent)
        }
    }
}
