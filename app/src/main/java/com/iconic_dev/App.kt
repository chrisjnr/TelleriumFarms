package com.iconic_dev

import android.app.Application
import com.iconic_dev.telleriumfarms.injection.appModule
import com.iconic_dev.telleriumfarms.injection.modules.allRepositoryViewModules
import com.iconic_dev.telleriumfarms.injection.modules.allViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by manuelchris-ogar on 24/10/2020.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, allRepositoryViewModules, allViewModelModule))
        }
    }
}