package com.iconic_dev.telleriumfarms.injection.modules

import com.iconic_dev.telleriumfarms.farmers.FarmersRepository
import org.koin.dsl.module

/**
 * Created by manuelchris-ogar on 24/10/2020.
 */
val allRepositoryViewModules = module{
    single { FarmersRepository(get(),get()) }
}