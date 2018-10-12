package com.troy.tersive.ui.read

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.troy.tersive.R
import com.troy.tersive.app.Injector
import kotlinx.android.synthetic.main.activity_read_list.*
import org.lds.mobile.extras.SelfActivityCompanion
import org.lds.mobile.livedata.observeNotNull
import org.lds.mobile.ui.ext.tintDrawable
import javax.inject.Inject

class ReadListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ReadListViewModel::class.java)
    }

    private val adapter by lazy { ReadListAdapter(this, viewModel) }

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_list)
        toolbar.setup()
        recyclerView.adapter = adapter
        viewModel.observe()
    }

    private fun ReadListViewModel.observe() {
        val activity = this@ReadListActivity
        docsLiveData.observeNotNull(activity) {
            adapter.submitList(it)
        }
        onClickEvent.observeNotNull(activity) {
            ReadActivity.startActivity(activity, it)
        }
    }

    private fun Toolbar.setup() {
        setTitle(R.string.practice_reading)
        navigationIcon = tintDrawable(R.drawable.ic_lds_arrow_back_24dp, R.color.white)
        setNavigationOnClickListener { finish() }
    }

    companion object : SelfActivityCompanion<Companion>(ReadListActivity::class)
}
