package com.iconic_dev.telleriumfarms.api.config

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by manuelchris-ogar on 26/06/2020.
 */
class ConnectivityCheckerInterceptor(private val context: Context):Interceptor{


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (!isInternetAvailable(context)){
            return Responses.noInternet(chain)
        }

        return chain.proceed(chain.request())
    }


    private fun isInternetAvailable(context: Context):Boolean{
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            cm?.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->  true
                    else -> false
                }
            }
        }else{
            cm?.run {
                cm.activeNetworkInfo?.run {
                    result = when(type){
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }


}