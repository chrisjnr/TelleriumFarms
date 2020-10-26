package com.iconic_dev.telleriumfarms.farmers


import com.google.gson.JsonElement
import com.iconic_dev.telleriumfarms.api.TelleriumApi
import com.iconic_dev.telleriumfarms.db.FarmersDatabase
import com.iconic_dev.telleriumfarms.db.models.Farmer
import com.iconic_dev.telleriumfarms.db.models.FarmerResponse
import io.reactivex.Observable

/**
 * Created by manuelchris-ogar on 24/10/2020.
 */
class FarmersRepository(private val telleriumApi: TelleriumApi, private val db:FarmersDatabase) {

    suspend fun getFarmersFromApi(limit:Int) = telleriumApi.getFarmers(limit = limit)


    suspend fun updateFarmer(farmer: Farmer) = db.sleepDatabaseDao.update(farmer)

    suspend fun addFarmer(farmer: Farmer) = db.sleepDatabaseDao.insert(farmer)


    fun getAllFarmers() = db.sleepDatabaseDao.getAll()

//    fun getSearchResultStream(number: Int): Flow<PagingData<Farmer>> {
//        return Pager(
//            config = PagingConfig(number),
//            pagingSourceFactory = { FarmerDataSource(this) }
//        ).flow
//    }


    fun executeNewsApi(index: Int): Observable<FarmerResponse?>? {
        return telleriumApi.fetchListNews(index)
    }


}