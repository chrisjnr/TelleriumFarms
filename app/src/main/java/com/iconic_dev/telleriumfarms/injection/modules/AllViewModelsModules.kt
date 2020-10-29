package com.iconic_dev.telleriumfarms.injection.modules

import com.iconic_dev.telleriumfarms.FarmersViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by manuelchris-ogar on 24/10/2020.
 */
val allViewModelModule = module {
    viewModel {
        FarmersViewModel(get(), get())
    }
}