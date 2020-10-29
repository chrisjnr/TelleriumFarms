package com.iconic_dev.telleriumfarms.farmers


import com.iconic_dev.telleriumfarms.api.TelleriumApi
import com.iconic_dev.telleriumfarms.db.FarmersDatabase
import com.iconic_dev.telleriumfarms.db.models.Farmer
import com.iconic_dev.telleriumfarms.db.models.FarmerResponse
import io.reactivex.Observable

/**
 * Created by manuelchris-ogar on 24/10/2020.
 */
class FarmersRepository(private val telleriumApi: TelleriumApi, private val db:FarmersDatabase) {


    suspend fun updateFarmer(farmer: Farmer) = db.sleepDatabaseDao.update(farmer)

    suspend fun addFarmer(farmer: Farmer) = db.sleepDatabaseDao.insert(farmer)


    fun getAllFarmers() = db.sleepDatabaseDao.getAll()

    fun executeNewsApi(index: Int): Observable<FarmerResponse?>? {
        return telleriumApi.fetchListNews(index)
    }


}