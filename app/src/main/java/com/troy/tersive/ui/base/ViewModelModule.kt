package com.troy.tersive.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.troy.tersive.ui.flashcard.FlashCardViewModel
import com.troy.tersive.ui.main.MainViewModel
import com.troy.tersive.ui.user.LoginViewModel
import com.troy.tersive.ui.user.RegisterViewModel
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
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel
}
