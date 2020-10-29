package com.iconic_dev.telleriumfarms.api.config

/**
 * Created by manuelchris-ogar on 28/06/2020.
 */
import android.util.Log
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

internal class NetworkResponseCall<S : Any, E : Any>(
        private val delegate: Call<S>,
        private val errorConverter: Converter<ResponseBody, E>
) : Call<NetworkResponse<S, E>> {

    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                Log.e("ViemodelCalls111", code.toString())
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(NetworkResponse.Success(body))
                        )
                    } else {
                        // Response is successful but the body is null
                        callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(NetworkResponse.UnknownError(null))
                        )
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    if (errorBody != null) {

                        when (code) {
                            401 -> {
                //                            failed refresh token or authentication
                //                            Log.e("ViemodelCalls111", "auth Finally Fäiled")
                //                            val modBody = response.body()

                                callback.onResponse(
                                        this@NetworkResponseCall,
                                        Response.success(NetworkResponse.ApiError(errorBody, code))
                                )

                            }
                            435 -> {
                                callback.onResponse(
                                        this@NetworkResponseCall,
                                        Response.success(NetworkResponse.NetworkError(Exception("Please check your internet connection and try again.")))
                                )
                            }
                            else -> {
                                callback.onResponse(
                                        this@NetworkResponseCall,
                                        Response.success(NetworkResponse.ApiError(errorBody, code))
                                )
                            }
                        }

                    } else {
                        callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(NetworkResponse.UnknownError(null))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is IOException -> NetworkResponse.NetworkError(throwable)
                    else -> NetworkResponse.UnknownError(throwable)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S, E>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

}


