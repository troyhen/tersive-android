package com.troy.tersive.ui.read

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.troy.tersive.R
import com.troy.tersive.app.Injector
import com.troy.tersive.model.data.WebDoc
import kotlinx.android.synthetic.main.activity_read.*
import me.eugeniomarletti.extras.intent.IntentExtra
import me.eugeniomarletti.extras.intent.base.String
import org.lds.mobile.extras.SelfActivityCompanion
import org.lds.mobile.livedata.observeNotNull
import org.lds.mobile.ui.ext.tintDrawable
import javax.inject.Inject

class ReadActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ReadViewModel::class.java)
    }

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        toolbar.setup()
        viewModel.observe()
    }

    private fun ReadViewModel.observe() {
        textLiveData.value = getString(R.string.loading)
        webUrl = intent.url
        textLiveData.observeNotNull(this@ReadActivity) {
            textView.text = it
        }
    }

    private fun Toolbar.setup() {
        title = intent.title
        navigationIcon = tintDrawable(R.drawable.ic_lds_arrow_back_24dp, R.color.white)
        setNavigationOnClickListener { finish() }
    }

    companion object : SelfActivityCompanion<Companion>(ReadActivity::class) {

        var Intent.url by IntentExtra.String()
        var Intent.title by IntentExtra.String()

        fun startActivity(context: Context, webDoc: WebDoc) {
            start(context) {
                it.url = webDoc.url
                it.title = webDoc.title
            }
        }
    }
}
