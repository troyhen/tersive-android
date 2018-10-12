package com.troy.tersive.ui.read

import com.troy.tersive.model.data.WebDoc
import com.troy.tersive.ui.base.BaseViewModel
import org.lds.mobile.coroutine.CoroutineContextProvider
import org.lds.mobile.livedata.SingleLiveEvent
import org.lds.mobile.livedata.mutableLiveData
import javax.inject.Inject

class ReadListViewModel @Inject constructor(cc: CoroutineContextProvider) : BaseViewModel(cc) {

    val docsLiveData = mutableLiveData(
        listOf(
            WebDoc(
                "https://www.gutenberg.org/files/74/74-0.txt",
                "The Adventures of Tom Sawyer",
                "Mark Twain"
            ),
            WebDoc(
                "https://www.gutenberg.org/files/11/11-0.txt",
                "Alice’s Adventures in Wonderland",
                "Lewis Carroll"
            ),
            WebDoc(
                "https://www.gutenberg.org/files/41/41-0.txt",
                "The Legend of Sleepy Hollow",
                "Washington Irving"
            ),
            WebDoc(
                "https://www.gutenberg.org/files/1342/1342-0.txt",
                "Pride and Prejudice",
                "Jane Austin"
            )
        )
    )

    val onClickEvent = SingleLiveEvent<WebDoc>()

    fun onClick(item: WebDoc) {
        onClickEvent.value = item
    }
}
