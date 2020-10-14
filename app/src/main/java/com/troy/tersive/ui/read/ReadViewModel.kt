package com.troy.tersive.ui.read

import androidx.lifecycle.viewModelScope
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.data.TersiveUtil
import com.troy.tersive.model.data.WebDoc
import com.troy.tersive.model.db.tersive.TersiveDatabaseManager
import com.troy.tersive.model.repo.WebRepo
import com.troy.tersive.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ReadViewModel(
    private val prefs: Prefs,
    private val tersiveMgr: TersiveDatabaseManager,
    private val tersiveUtil: TersiveUtil,
    private val webRepo: WebRepo
) : BaseViewModel<Unit>() {

    var webDoc: WebDoc? = null
        set(value) {
            field = value
            load()
        }

    val textFlow = MutableStateFlow<CharSequence>("")
    val tersiveFlow = MutableStateFlow<CharSequence>("")

    private fun load() = viewModelScope.launch {
        val phrases = tersiveMgr.tersiveDb.tersiveDao.findAll()
        tersiveUtil.loadPhrases(phrases, prefs.typeMode)
        val original = webDoc?.let { webRepo.load(it) } ?: ""
        val tersive = tersiveUtil.toTersive(original)
        textFlow.value = original
        tersiveFlow.value = tersive
    }
}
