package com.troy.tersive.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.troy.tersive.ui.flashcard.FlashCardViewModel
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
    internal abstract fun bindAlbumViewModel(viewModel: FlashCardViewModel): ViewModel
}
