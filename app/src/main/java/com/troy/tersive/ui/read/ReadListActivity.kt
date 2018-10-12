package com.troy.tersive.ui.read

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.troy.tersive.R
import com.troy.tersive.app.Injector
import kotlinx.android.synthetic.main.activity_read_list.*
import org.lds.mobile.extras.SelfActivityCompanion
import org.lds.mobile.livedata.observeNotNull
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
        recyclerView.adapter = adapter
        viewModel.setupObservers()
    }

    private fun ReadListViewModel.setupObservers() {
        docsLiveData.observeNotNull(this@ReadListActivity) {
            adapter.submitList(it)
        }
        onClickEvent.observeNotNull(this@ReadListActivity) {

        }
    }

    companion object : SelfActivityCompanion<Companion>(ReadListActivity::class)
}
