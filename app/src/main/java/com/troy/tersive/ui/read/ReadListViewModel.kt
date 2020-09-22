package com.troy.tersive.ui.read

import com.troy.tersive.model.data.WebDoc
import com.troy.tersive.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ReadListViewModel() : BaseViewModel<ReadListViewModel.Event>() {

    val docsFlow: Flow<List<WebDoc>> = MutableStateFlow(sampleDocs)

    fun onClick(item: WebDoc) {
        send(Event.ClickEvent(item))
    }

    companion object {
        val sampleDocs = listOf(
            WebDoc(
                "https://www.gutenberg.org/files/74/74-0.txt",
                "The Adventures of Tom Sawyer",
                "Mark Twain",
                "PREFACE",
                "part of their lives at present."
            ),
            WebDoc(
                "http://www.gutenberg.org/files/11/11-0.txt",
                "Alice’s Adventures in Wonderland",
                "Lewis Carroll",
                "CHAPTER I.",
                "              THE END"
            ),
            WebDoc(
                "https://www.gutenberg.org/files/41/41-0.txt",
                "The Legend of Sleepy Hollow",
                "Washington Irving",
                "FOUND AMONG THE PAPERS",
                "THE END."
            ),
            WebDoc(
                "https://www.gutenberg.org/files/16/16-0.txt",
                "Peter Pan",
                "James M. Barrie",
                "Chapter 1 PETER BREAKS THROUGH\r\n\r\nAll children",
                "THE END"
            ),
            WebDoc(
                "https://www.gutenberg.org/files/1342/1342-0.txt",
                "Pride and Prejudice",
                "Jane Austin",
                "Chapter 1",
                "Derbyshire, had been the means of uniting them."
            )
        )
    }

    sealed class Event {
        data class ClickEvent(val doc: WebDoc) : Event()
    }
}
