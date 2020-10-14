package com.troy.tersive.ui.read

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.troy.tersive.R
import com.troy.tersive.model.ext.collectWhenStarted
import com.troy.tersive.model.ext.receiveWhenStarted
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ReadListActivity : AppCompatActivity() {

    private val viewModel: ReadListViewModel = getViewModel()

    private val adapter by lazy { ReadListAdapter(this, viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_list)
//        toolbar.setup()
//        recyclerView.adapter = adapter
        viewModel.observe()
    }

    private fun ReadListViewModel.observe() {
        val activity = this@ReadListActivity
        collectWhenStarted(docsFlow) {
            adapter.submitList(it)
        }
        receiveWhenStarted(eventChannel) { event ->
            when (event) {
                is ReadListViewModel.Event.ClickEvent -> ReadActivity.startActivity(activity, event.doc)
            }
        }
    }

    private fun Toolbar.setup() {
        setTitle(R.string.practice_reading)
//        navigationIcon = tintDrawable(R.drawable.ic_lds_arrow_back_24dp, R.color.white)
        setNavigationOnClickListener { finish() }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ReadListActivity::class.java))
        }
    }
}
