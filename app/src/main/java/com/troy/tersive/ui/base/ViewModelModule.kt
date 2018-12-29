package com.troy.tersive.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.troy.tersive.ui.flashcard.FlashCardViewModel
import com.troy.tersive.ui.main.MainViewModel
import com.troy.tersive.ui.read.ReadListViewModel
import com.troy.tersive.ui.read.ReadViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.lds.mobile.inject.viewmodel.ViewModelFactory
import org.lds.mobile.inject.viewmodel.ViewModelKey

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlashCardViewModel::class)
    internal abstract fun bindFlashCardViewModel(viewModel: FlashCardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReadListViewModel::class)
    internal abstract fun bindReadListViewModel(viewModel: ReadListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReadViewModel::class)
    internal abstract fun bindReadViewModel(viewModel: ReadViewModel): ViewModel
}
