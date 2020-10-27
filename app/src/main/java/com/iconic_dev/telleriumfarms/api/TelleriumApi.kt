package com.iconic_dev.telleriumfarms.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.iconic_dev.telleriumfarms.BuildConfig
import com.iconic_dev.telleriumfarms.api.config.NetworkResponse
import com.iconic_dev.telleriumfarms.db.models.FarmerResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by manuelchris-ogar on 24/10/2020.
 */
interface TelleriumApi {


    companion object {
        const val API_BASE_URL = BuildConfig.base_url
    }


    @GET("get-sample-farmers")
    fun fetchListNews(
        @Query("limit") limit: Int
    ): Observable<FarmerResponse?>?

    @GET("get-sample-farmers")
    suspend fun getFarmers(@Query("limit") limit: Int):NetworkResponse<FarmerResponse, JsonObject>
}