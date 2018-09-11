package com.troy.tersive.app

//import androidx.work.WorkManager
//import org.lds.gliv.mgr.NetworkManager
//import org.lds.gliv.mgr.analytics.Analytics
//import org.lds.gliv.mgr.analytics.DefaultAnalytics
//import org.lds.gliv.ux.auth.AuthModule
//import org.lds.ldsaccount.NetworkConnectionManager
//import org.lds.mobile.media.cast.CastManager
import android.app.Application
import com.troy.tersive.mgr.AsyncRunner
import dagger.Module
import dagger.Provides
import org.lds.mobile.coroutine.CoroutineContextProvider
import org.threeten.bp.Clock
import javax.inject.Singleton

@Module//(includes = [AuthModule::class])
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication() = application

//    @Provides
//    @Singleton
//    fun provideAnalytics(application: Application): Analytics {
//        return DefaultAnalytics(application)
//    }

//    @Provides
//    @Singleton
//    fun provideCastManager(application: Application) = CastManager(application, initCast = true, castFromDeviceEnabled = true)

    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.systemDefaultZone()

    @Provides
    @Singleton
    fun provideCoroutineContextProvider(): CoroutineContextProvider =
        CoroutineContextProvider.MainCoroutineContextProvider

    @Provides
    @Singleton
    fun provideAsyncRunner(): AsyncRunner = AsyncRunner.MainRunner

//    @Provides
//    fun provideConnectivityManager(application: Application): ConnectivityManager {
//        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    }

//    @Provides
//    @Singleton
//    fun provideNetworkConnectionManager(networkManager: NetworkManager): NetworkConnectionManager {
//        return networkManager
//    }

//    @Provides
//    @Singleton
//    fun provideWorkManager(): WorkManager {
//        return WorkManager.getInstance()!!  // won't be null because we have not disabled automatic configuration
//    }
}
