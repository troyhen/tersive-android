package com.troy.tersive.app.init

import android.content.Context
import androidx.startup.Initializer
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.data.HashUtil
import com.troy.tersive.model.data.TersiveUtil
import com.troy.tersive.model.db.tersive.TersiveDatabaseManager
import com.troy.tersive.model.db.user.UserDatabaseManager
import com.troy.tersive.model.repo.FirestoreRepo
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.model.repo.UserRepo
import com.troy.tersive.model.repo.WebRepo
import com.troy.tersive.model.util.EncryptUtil
import com.troy.tersive.ui.admin.AdminMenuViewModel
import com.troy.tersive.ui.flashcard.FlashCardViewModel
import com.troy.tersive.ui.main.MainViewModel
import com.troy.tersive.ui.read.ReadListViewModel
import com.troy.tersive.ui.read.ReadViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinInit : Initializer<Unit> {

    override fun create(context: Context) {
        val applicationContext = checkNotNull(context.applicationContext) { "Missing Application Context" }
        startKoin {
            androidContext(applicationContext)
            modules(appModule)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    companion object {
        val appModule = module {
            single { EncryptUtil() }
            single { FlashCardRepo(get(), get(), get(), get()) }
            single { FirestoreRepo(get()) }
            single { HashUtil() }
            single { Prefs() }
            single { TersiveDatabaseManager(get()) }
            single { TersiveUtil(get()) }
            single { UserDatabaseManager(get()) }
            single { UserRepo(get(), get(), get()) }
            single { WebRepo() }

            viewModel { AdminMenuViewModel() }
            viewModel { FlashCardViewModel(get(), get(), get(), get()) }
            viewModel { MainViewModel(get(), get()) }
            viewModel { ReadListViewModel() }
            viewModel { ReadViewModel(get(), get(), get(), get()) }
        }
    }
}
