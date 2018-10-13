package com.troy.tersive.ui.read

import androidx.lifecycle.MutableLiveData
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.data.TersiveUtil
import com.troy.tersive.model.data.WebDoc
import com.troy.tersive.model.db.tersive.TersiveDatabaseManager
import com.troy.tersive.model.repo.WebRepo
import com.troy.tersive.ui.base.BaseViewModel
import kotlinx.coroutines.experimental.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import javax.inject.Inject

class ReadViewModel @Inject constructor(
    cc: CoroutineContextProvider,
    private val prefs: Prefs,
    private val tersiveMgr: TersiveDatabaseManager,
    private val tersiveUtil: TersiveUtil,
    private val webRepo: WebRepo
) : BaseViewModel(cc) {

    var webDoc: WebDoc? = null
        set(value) {
            field = value
            load()
        }

    val textLiveData = MutableLiveData<CharSequence>()
    val tersiveLiveData = MutableLiveData<CharSequence>()

    private fun load() = launch {
        val phrases = tersiveMgr.tersiveDb.tersiveDao.findAll()
        tersiveUtil.loadPhrases(phrases, prefs.typeMode)
        val original = webDoc?.let { webRepo.load(it) }
        val tersive = original?.let { tersiveUtil.toTersive(it) }
        textLiveData.postValue(original)
        tersiveLiveData.postValue(tersive)
    }
}
