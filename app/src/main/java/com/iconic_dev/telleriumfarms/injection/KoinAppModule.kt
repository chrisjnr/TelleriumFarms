package com.iconic_dev.telleriumfarms.injection

import android.content.Context
import com.iconic_dev.telleriumfarms.BuildConfig
import com.iconic_dev.telleriumfarms.FarmersViewModel
import com.iconic_dev.telleriumfarms.api.TelleriumApi
import com.iconic_dev.telleriumfarms.db.FarmersDatabase
import com.iconic_dev.telleriumfarms.db.models.Farmer
import com.iconic_dev.telleriumfarms.api.config.ConnectivityCheckerInterceptor
import com.iconic_dev.telleriumfarms.farmers.FarmersRepository
import com.iconic_dev.telleriumfarms.farmers.api.FarmersDataSourceFactory
import com.iconic_dev.telleriumfarms.injection.modules.allRepositoryViewModules
import com.iconic_dev.wecycler.api.config.NetworkResponseAdapterFactory
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by manuelchris-ogar on 24/10/2020.
 */
    private fun provideApiService(retrofit: Retrofit): TelleriumApi = retrofit.create(TelleriumApi::class.java)


    private fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .baseUrl(TelleriumApi.API_BASE_URL)
            .client(okHttpClient)
            .build()


    private fun provideConnectivityCheckerInterceptor(context: Context): ConnectivityCheckerInterceptor = ConnectivityCheckerInterceptor(context)


    private fun provideOkHttpClient(connectivityCheckerInterceptor: ConnectivityCheckerInterceptor) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(connectivityCheckerInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .addInterceptor(connectivityCheckerInterceptor)
        .build()


    private fun provideFarmer() = Farmer()


//    private fun provideFarmersDataSourceFactory(farmer: Farmer, viewModel: FarmersViewModel) = FarmersDataSourceFactory(farmer, viewModel)


//    private fun provideFarmerDataSource(viewModel: FarmersViewModel)

//  private fun provide


    private fun provideDb(context: Context):FarmersDatabase  = FarmersDatabase.getInstance(context)

    val appModule = module {
        single { provideConnectivityCheckerInterceptor(get()) }
        single { provideOkHttpClient(get()) }
        single { provideRetrofit(get()) }
        single { provideApiService(get()) }
        single { provideDb(androidContext()) }
        single { provideFarmer() }
//        single { provideFarmersDataSourceFactory(get(), get()) }
    }
